#!/bin/bash

# Checks that the configuration a security mode needs is present, and says
# what is missing. Never prints a secret or a token, only whether it is there.
#
# Usage: check-config.sh <keycloak|github|none> [--for-test]
#
# Values come from the environment, else from ENV_FILE (docker.conf/.env by
# default), the same precedence as docker compose. With --for-test, also checks
# what the security tests need: the docker image, and a bearer token that the
# service should accept (TOKEN_FILE, by default ~/.keycloak_token or
# ~/.github_token).

MODE=${1:-}
FOR_TEST=false
[ "${2:-}" = "--for-test" ] && FOR_TEST=true
ENV_FILE=${ENV_FILE:-docker.conf/.env}

ERRORS=0
WARNINGS=0

ok()      { echo "  ok       $*"; }
missing() { echo "  MISSING  $*"; ERRORS=$((ERRORS+1)); }
bad()     { echo "  WRONG    $*"; ERRORS=$((ERRORS+1)); }
warn()    { echo "  WARNING  $*"; WARNINGS=$((WARNINGS+1)); }

case "$MODE" in
    keycloak|github|none) ;;
    *) echo "Usage: $0 <keycloak|github|none> [--for-test]" 1>&2; exit 2 ;;
esac

# the value of a variable: the environment first, else ENV_FILE
lookup() {
    local name=$1
    if [ -n "${!name+set}" ]; then
        printf '%s' "${!name}"
        return 0
    fi
    [ -r "$ENV_FILE" ] || return 1
    local line
    line=$(grep -E "^[[:space:]]*(export[[:space:]]+)?$name=" "$ENV_FILE" | tail -1) || return 1
    line=${line#*=}
    line=${line%\"}; line=${line#\"}
    line=${line%\'}; line=${line#\'}
    printf '%s' "$line"
}

is_set()       { lookup "$1" >/dev/null; }
is_non_empty() { local v; v=$(lookup "$1") && [ -n "$v" ]; }

require() {
    local name=$1 what=$2
    if is_non_empty "$name"; then ok "$name"; else missing "$name: $what"; fi
}

echo "Configuration for TPL_SECURITY_MODE=$MODE (environment, then $ENV_FILE)"

if [ -r "$ENV_FILE" ]; then
    ok "$ENV_FILE exists"
    perms=$(stat -f '%Lp' "$ENV_FILE" 2>/dev/null || stat -c '%a' "$ENV_FILE" 2>/dev/null)
    case "$perms" in
        *00) ;;
        *) warn "$ENV_FILE is readable by others (mode $perms): chmod 600 $ENV_FILE" ;;
    esac
    if git -C "$(dirname "$ENV_FILE")" ls-files --error-unmatch "$(basename "$ENV_FILE")" >/dev/null 2>&1; then
        bad "$ENV_FILE is tracked by git: git rm --cached it, it holds secrets"
    fi
else
    warn "no $ENV_FILE: values must then all be in the environment (cp docker.conf/env.example $ENV_FILE)"
fi

echo "Database"
require POSTGRES_PASSWORD "password of the postgres superuser, used to create the service's database"
require TPL_DB_USER "database account of the service"
require TPL_DB_PASS "password of that account"

if [ "$MODE" != none ]; then
    echo "Service"
    if is_non_empty TPL_PUBLIC_URL; then
        ok "TPL_PUBLIC_URL=$(lookup TPL_PUBLIC_URL)"
    else
        warn "TPL_PUBLIC_URL unset: http://localhost:<TPL_HTTP_PORT> is assumed for the login callback"
    fi
fi

if [ "$MODE" = keycloak ]; then
    echo "Keycloak"
    require KEYCLOAK_BASE_URI "Keycloak's base URL, before /realms (e.g. https://keycloak.example.org/auth)"
    require KEYCLOAK_REALM "the realm"
    require KEYCLOAK_CLIENT_ID "public client for browser login"
    require KEYCLOAK_DIRECT_CLIENT_ID "client id(s) whose access tokens are accepted as bearer tokens"
    if is_non_empty KEYCLOAK_BASE_URI && is_non_empty KEYCLOAK_REALM; then
        REALM_URI="$(lookup KEYCLOAK_BASE_URI)/realms/$(lookup KEYCLOAK_REALM)"
        if curl -fsS --max-time 15 "$REALM_URI" 2>/dev/null | grep -q '"public_key"'; then
            ok "realm $REALM_URI answers with its public key"
        elif is_non_empty KEYCLOAK_SIGNATURE_PUBLIC_KEY; then
            warn "realm $REALM_URI unreachable from here; KEYCLOAK_SIGNATURE_PUBLIC_KEY is set"
        else
            bad "realm $REALM_URI unreachable, and no KEYCLOAK_SIGNATURE_PUBLIC_KEY"
        fi
    fi
fi

if [ "$MODE" = github ]; then
    echo "GitHub"
    require GITHUB_CLIENT_ID "client id of the GitHub OAuth App"
    require GITHUB_CLIENT_SECRET "client secret of that OAuth App"
    if is_set GITHUB_ALLOWLIST; then
        if is_non_empty GITHUB_ALLOWLIST; then ok "GITHUB_ALLOWLIST"; else warn "GITHUB_ALLOWLIST is empty: nobody is admitted"; fi
    else
        missing "GITHUB_ALLOWLIST: comma-separated GitHub logins, github:<numeric id>, or *"
    fi
fi

if [ "$MODE" = none ]; then
    warn "TPL_SECURITY_MODE=none: every endpoint is open to anyone who can reach the port"
fi

if $FOR_TEST; then
    echo "Tests"
    if ! command -v docker >/dev/null; then
        missing "docker: not installed"
    elif ! docker info >/dev/null 2>&1; then
        missing "docker: the daemon is not running"
    else
        IMAGE=$(lookup TPL_IMAGE)
        IMAGE=${IMAGE:-${DEFAULT_IMAGE:-}}
        if [ -z "$IMAGE" ]; then
            missing "TPL_IMAGE: the image to test"
        elif docker image inspect "$IMAGE" >/dev/null 2>&1; then
            ok "image $IMAGE"
        else
            missing "image $IMAGE: build it with make image"
        fi
    fi
    for tool in curl jq; do
        command -v $tool >/dev/null && ok "$tool" || missing "$tool: not installed"
    done

    if [ "$MODE" = keycloak ]; then
        TOKEN_FILE=${TOKEN_FILE:-$HOME/.keycloak_token}
        if [ ! -s "$TOKEN_FILE" ]; then
            missing "TOKEN_FILE=$TOKEN_FILE: an access token of a KEYCLOAK_DIRECT_CLIENT_ID client (Keycloak tokens last minutes: get a fresh one just before the test)"
        else
            claims=$(cut -d. -f2 < "$TOKEN_FILE" | tr '_-' '/+' | jq -R '. + ("=" * ((4 - length % 4) % 4)) | @base64d | fromjson' 2>/dev/null)
            if [ -z "$claims" ]; then
                bad "TOKEN_FILE=$TOKEN_FILE is not a JWT"
            else
                left=$(( $(jq -r '.exp' <<< "$claims") - $(date +%s) ))
                if [ "$left" -lt 60 ]; then
                    bad "the token in $TOKEN_FILE has expired, or expires within a minute ($left s): get a fresh one"
                else
                    ok "token in $TOKEN_FILE valid for $left s more"
                fi
                if is_non_empty KEYCLOAK_BASE_URI && is_non_empty KEYCLOAK_REALM; then
                    iss=$(jq -r '.iss' <<< "$claims")
                    [ "$iss" = "$REALM_URI" ] && ok "token issued by $iss" || bad "token issued by $iss, not by $REALM_URI"
                fi
                if is_non_empty KEYCLOAK_DIRECT_CLIENT_ID; then
                    azp=$(jq -r '.azp' <<< "$claims")
                    case ",$(lookup KEYCLOAK_DIRECT_CLIENT_ID | tr -d ' ')," in
                        *",$azp,"*) ok "token of client $azp" ;;
                        *) bad "token of client $azp, not one of KEYCLOAK_DIRECT_CLIENT_ID" ;;
                    esac
                fi
            fi
        fi
    fi

    if [ "$MODE" = github ]; then
        TOKEN_FILE=${TOKEN_FILE:-$HOME/.github_token}
        if [ ! -s "$TOKEN_FILE" ]; then
            missing "TOKEN_FILE=$TOKEN_FILE: a GitHub token of an allowlisted user (see bin/github-get-token.sh of the service)"
        else
            # the header read from stdin, so that the token is not on a command line
            user=$(echo "Authorization: Bearer $(cat "$TOKEN_FILE")" | curl -fsS --max-time 15 -H @- https://api.github.com/user 2>/dev/null)
            login=$(jq -r '.login // empty' <<< "$user" 2>/dev/null)
            id=$(jq -r '.id // empty' <<< "$user" 2>/dev/null)
            if [ -z "$login" ]; then
                bad "GitHub refuses the token in $TOKEN_FILE (expired or revoked?)"
            else
                ok "token in $TOKEN_FILE belongs to $login"
                case ",$(lookup GITHUB_ALLOWLIST | tr -d ' ')," in
                    *",*,"*|*",$login,"*|*",github:$id,"*) ok "$login is allowlisted" ;;
                    *) bad "$login is not in GITHUB_ALLOWLIST: the service will refuse the token" ;;
                esac
            fi
        fi
    fi
fi

echo
if [ "$ERRORS" -gt 0 ]; then
    echo "$ERRORS problem(s), $WARNINGS warning(s): see README.md for each value"
    exit 1
fi
echo "Configuration complete for $MODE ($WARNINGS warning(s))"
