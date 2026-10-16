#!/bin/bash

# Security test of the template service container, for one security mode.
#
# Usage: test-security.sh <keycloak|github>
#
# Starts the compose stack (docker.conf/docker-compose.yml) under its own
# project name, port and data folder, so that it does not touch a running
# deployment, then checks that:
#   - protected endpoints (POST provapi/templates/records, POST
#     provapi/statements, GET forms.html) refuse or redirect to the identity provider a request
#     without credentials, or with a malformed or forged bearer token;
#   - they accept a valid bearer token (TOKEN_FILE);
#   - a valid token is refused once the service no longer trusts it
#     (keycloak: another bearer client id; github: an allowlist without
#     the token's user);
#   - neither the image nor the logs carry the configured secrets;
# and stops the stack (KEEP=true leaves it running).
#
# Configuration as for make config.check: the environment, then ENV_FILE.
# Environment: ENV_FILE, COMPOSE_FILE, TPL_CONTEXT, TPL_IMAGE, TEST_PORT,
# TOKEN_FILE, TEST_DATA_DIR, KEEP, STARTUP_TIMEOUT, BASE_RELATION (records are
# searched for; by default the first the database knows), STATEMENT_FILE (a
# JSON statement of the catalogue, posted with the valid token, which must then
# be accepted with 2xx; without it, the valid token's POST of an empty list
# must only get past the security filter).

set -u

MODE=${1:-}
case "$MODE" in
    keycloak|github) ;;
    *) echo "Usage: $0 <keycloak|github>" 1>&2; exit 2 ;;
esac

ENV_FILE=${ENV_FILE:-docker.conf/.env}
COMPOSE_FILE=${COMPOSE_FILE:-docker.conf/docker-compose.yml}
CONTEXT=${TPL_CONTEXT:?TPL_CONTEXT, the service context such as /book}
TEST_PORT=${TEST_PORT:-18080}
STARTUP_TIMEOUT=${STARTUP_TIMEOUT:-300}
KEEP=${KEEP:-false}
BASE_RELATION=${BASE_RELATION:-}
STATEMENT_FILE=${STATEMENT_FILE:-}
PROJECT="tpl-security-test-$MODE"
TEST_DATA_DIR=${TEST_DATA_DIR:-$(pwd)/target/security-test/$MODE}
case "$MODE" in
    keycloak) TOKEN_FILE=${TOKEN_FILE:-$HOME/.keycloak_token} ;;
    github)   TOKEN_FILE=${TOKEN_FILE:-$HOME/.github_token} ;;
esac

BASE="http://localhost:$TEST_PORT$CONTEXT"
TMP=$(mktemp -d)
FAILED=0
PASSED=0

# the value of a variable: the environment first, else ENV_FILE
lookup() {
    local name=$1
    if [ -n "${!name+set}" ]; then printf '%s' "${!name}"; return 0; fi
    [ -r "$ENV_FILE" ] || return 1
    local line
    line=$(grep -E "^[[:space:]]*(export[[:space:]]+)?$name=" "$ENV_FILE" | tail -1) || return 1
    line=${line#*=}
    line=${line%\"}; line=${line#\"}
    line=${line%\'}; line=${line#\'}
    printf '%s' "$line"
}

IMAGE=$(lookup TPL_IMAGE)
IMAGE=${IMAGE:-${DEFAULT_IMAGE:?TPL_IMAGE or DEFAULT_IMAGE, the image to test}}

compose() {
    local args=(--project-name "$PROJECT" -f "$COMPOSE_FILE")
    [ -r "$ENV_FILE" ] && args+=(--env-file "$ENV_FILE")
    TPL_IMAGE=$IMAGE \
    TPL_SECURITY_MODE=$MODE \
    TPL_HTTP_PORT=$TEST_PORT \
    TPL_PUBLIC_URL=http://localhost:$TEST_PORT \
    TPL_DATA_DIR=$TEST_DATA_DIR \
    TPL_PROJECT=$PROJECT \
    TPL_SECURITY_CONFIG= \
        docker compose "${args[@]}" "$@"
}

clean_data() {
    # the database files belong to the containers' users: remove them from a container
    [ -d "$TEST_DATA_DIR" ] && docker run --rm --entrypoint sh -v "$TEST_DATA_DIR:/data" postgres:16 -c 'rm -rf /data/* /data/.[!.]*' >/dev/null 2>&1
    rm -rf "$TEST_DATA_DIR"
}

finish() {
    if [ "$KEEP" = true ]; then
        echo "KEEP=true: stack $PROJECT left running on $BASE (make test.down MODE=$MODE to stop it)"
    else
        compose down -v --remove-orphans >/dev/null 2>&1
        clean_data
    fi
    rm -rf "$TMP"
}
trap finish EXIT

pass() { echo "  PASS  $*"; PASSED=$((PASSED+1)); }
fail() { echo "  FAIL  $*"; FAILED=$((FAILED+1)); }

# request <name> <method> <path> [credential]: sets STATUS and LOCATION
# credential: none, a token file, or the literal header value after "Bearer "
request() {
    local name=$1 method=$2 path=$3 credential=${4:-none}
    local headers="$TMP/$name.headers" body="$TMP/$name.body"
    local data=()
    case "$method $path" in
        "POST /provapi/templates/records")
            # the search of the dashboard's record browser
            data=(-H 'Content-Type: application/json' -H 'Accept: text/csv'
                  --data-binary "{\"base_relation\":\"$BASE_RELATION\",\"from_date\":null,\"to_date\":null,\"limit\":5}")
            ;;
        "POST /provapi/statements")
            data=(-H 'Content-Type: application/vnd.kcl.prov-template+json'
                  -H 'Accept: application/vnd.kcl.prov-template+json')
            if [ -n "$STATEMENT_FILE" ]; then
                data+=(--data-binary @"$STATEMENT_FILE")
            else
                data+=(--data-binary '[]')
            fi
            ;;
    esac
    : > "$TMP/auth"
    if [ "$credential" != none ]; then
        if [ -f "$credential" ]; then
            echo "Authorization: Bearer $(cat "$credential")" > "$TMP/auth"
        else
            echo "Authorization: Bearer $credential" > "$TMP/auth"
        fi
    fi
    STATUS=$(curl -s -o "$body" -D "$headers" -w '%{http_code}' --max-time 60 \
                  -X "$method" -H @"$TMP/auth" "${data[@]}" "$BASE$path")
    LOCATION=$(grep -i '^location:' "$headers" | tail -1 | cut -d' ' -f2- | tr -d '\r')
}

# the request must not reach the endpoint: 401, 403, or a redirect to the identity provider
expect_refused() {
    local label=$1
    case "$STATUS" in
        401|403) pass "$label: refused ($STATUS)" ;;
        302|303)
            case "$LOCATION" in
                "$IDP_LOGIN"*) pass "$label: redirected to the login of $IDP_NAME" ;;
                *) fail "$label: redirected to $LOCATION, not to $IDP_LOGIN" ;;
            esac
            ;;
        *) fail "$label: answered $STATUS, should be refused" ;;
    esac
}

# the request must reach the service: anything but a refusal or a redirect
expect_past_filter() {
    local label=$1
    case "$STATUS" in
        401|403|3??|000) fail "$label: answered $STATUS${LOCATION:+ (to $LOCATION)}, should reach the service" ;;
        *) pass "$label: reached the service ($STATUS; set STATEMENT_FILE to a valid statement to require 2xx)" ;;
    esac
}

# the request must pass the security filter: 2xx
expect_accepted() {
    local label=$1
    case "$STATUS" in
        2??) pass "$label: accepted ($STATUS)" ;;
        *) fail "$label: answered $STATUS${LOCATION:+ (to $LOCATION)}, should be accepted" ;;
    esac
}

wait_ready() {
    local deadline=$(( $(date +%s) + STARTUP_TIMEOUT )) code state
    while [ "$(date +%s)" -lt "$deadline" ]; do
        code=$(curl -s -o /dev/null -w '%{http_code}' --max-time 5 "$BASE/")
        [ "$code" = 200 ] && return 0
        state=$(docker inspect -f '{{.State.Status}} {{.RestartCount}}' "$(compose ps -q provtemplates 2>/dev/null)" 2>/dev/null)
        case "$state" in
            restarting*|exited*|"running "[1-9]*)
                echo "the service container does not stay up ($state):"
                compose logs --no-color --tail 40 provtemplates
                return 1 ;;
        esac
        sleep 3
    done
    echo "the service did not answer $BASE/ within $STARTUP_TIMEOUT s:"
    compose logs --no-color --tail 60 provtemplates
    return 1
}

# start (or restart) the service with extra environment overrides
start() {
    ( export "$@"; compose up -d --force-recreate --no-deps provtemplates ) >/dev/null 2>&1
}

forged_token() {
    # the header and claims of the valid token, with another signature
    local header claims
    header=$(cut -d. -f1 < "$TOKEN_FILE")
    claims=$(cut -d. -f2 < "$TOKEN_FILE")
    printf '%s.%s.%s' "$header" "$claims" "$(head -c 256 /dev/urandom | base64 | tr -d '=\n' | tr '/+' '_-')"
}

case "$MODE" in
    keycloak)
        KEYCLOAK_BASE_URI=$(lookup KEYCLOAK_BASE_URI)
        KEYCLOAK_REALM=$(lookup KEYCLOAK_REALM)
        IDP_NAME="Keycloak realm $KEYCLOAK_REALM"
        IDP_LOGIN="$KEYCLOAK_BASE_URI/realms/$KEYCLOAK_REALM/protocol/openid-connect/auth"
        ;;
    github)
        IDP_NAME="GitHub"
        IDP_LOGIN="https://github.com/login/oauth/authorize"
        ;;
esac

echo "Security test, mode $MODE, image $IMAGE, on $BASE (project $PROJECT)"

compose down -v --remove-orphans >/dev/null 2>&1
clean_data
mkdir -p "$TEST_DATA_DIR"

echo "Starting the stack"
if ! compose up -d >"$TMP/up.log" 2>&1; then
    cat "$TMP/up.log"
    echo "FAILED: the stack did not start"
    exit 1
fi
wait_ready || { echo "FAILED: the service did not start"; exit 1; }
echo "Service up"
# the service creates its search functions as it starts: wait for them a little
for attempt in 1 2 3 4 5 6 7 8 9 10 11 12; do
    [ -n "$BASE_RELATION" ] && break
    BASE_RELATION=$(compose exec -T postgres16 psql -U postgres -d templates -Atc \
        "select substr(proname, 20) from pg_proc where proname like 'search\\_records\\_for\\_%' order by 1 limit 1" 2>/dev/null | tr -d '\r')
    [ -n "$BASE_RELATION" ] || sleep 5
done
[ -n "$BASE_RELATION" ] || { echo "FAILED: no base relation to search records for (set BASE_RELATION)"; exit 1; }
echo "Records searched for base relation $BASE_RELATION${STATEMENT_FILE:+, statement from $STATEMENT_FILE}"

echo "Without credentials"
request records POST /provapi/templates/records
expect_refused "POST provapi/templates/records"
case "$LOCATION" in
    *client_id=*)
        expected_client=$( [ "$MODE" = keycloak ] && lookup KEYCLOAK_CLIENT_ID || lookup GITHUB_CLIENT_ID )
        case "$LOCATION" in
            *"client_id=$expected_client"*) pass "login redirect names client $expected_client" ;;
            *) fail "login redirect does not name client $expected_client: $LOCATION" ;;
        esac ;;
esac
request statements POST /provapi/statements
expect_refused "POST provapi/statements"
request forms GET /forms.html
expect_refused "GET forms.html"
request index GET /
expect_accepted "GET / (public index)"

echo "With a malformed bearer token"
request records-bad POST /provapi/templates/records "not-a-token"
expect_refused "POST provapi/templates/records"
request statements-bad POST /provapi/statements "not-a-token"
expect_refused "POST provapi/statements"

if [ "$MODE" = keycloak ]; then
    echo "With a forged bearer token (valid claims, wrong signature)"
    request records-forged POST /provapi/templates/records "$(forged_token)"
    expect_refused "POST provapi/templates/records"
fi

echo "With a valid bearer token ($TOKEN_FILE)"
request records-ok POST /provapi/templates/records "$TOKEN_FILE"
expect_accepted "POST provapi/templates/records"
request statements-ok POST /provapi/statements "$TOKEN_FILE"
if [ -n "$STATEMENT_FILE" ]; then
    expect_accepted "POST provapi/statements"
else
    expect_past_filter "POST provapi/statements"
fi
request forms-ok GET /forms.html "$TOKEN_FILE"
expect_accepted "GET forms.html"

echo "No secret in the image or the logs"
docker image inspect "$IMAGE" > "$TMP/image.json"
docker history --no-trunc "$IMAGE" > "$TMP/history.txt"
compose logs --no-color > "$TMP/logs.txt" 2>&1
for name in POSTGRES_PASSWORD TPL_DB_PASS GITHUB_CLIENT_SECRET; do
    value=$(lookup $name)
    [ ${#value} -ge 4 ] || continue
    if grep -qF -- "$value" "$TMP/image.json" "$TMP/history.txt"; then
        fail "$name is in the image $IMAGE"
    else
        pass "$name is not in the image"
    fi
    if grep -qF -- "$value" "$TMP/logs.txt"; then
        fail "$name appears in the container logs"
    else
        pass "$name does not appear in the logs"
    fi
done
if grep -qF -- "$(cat "$TOKEN_FILE")" "$TMP/logs.txt"; then
    fail "the bearer token appears in the container logs"
else
    pass "the bearer token does not appear in the logs"
fi

echo "A valid token the service no longer trusts"
case "$MODE" in
    keycloak) start KEYCLOAK_DIRECT_CLIENT_ID=no-such-client-for-test ;;
    github)   start GITHUB_ALLOWLIST=no-such-user-for-test ;;
esac
if wait_ready; then
    request records-untrusted POST /provapi/templates/records "$TOKEN_FILE"
    expect_refused "POST provapi/templates/records"
    request statements-untrusted POST /provapi/statements "$TOKEN_FILE"
    expect_refused "POST provapi/statements"
else
    fail "the service did not restart"
fi

echo
echo "$MODE: $PASSED passed, $FAILED failed"
[ "$FAILED" -eq 0 ]
