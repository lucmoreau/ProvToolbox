#!/bin/sh

# Entry point of the template service image.
#
# The image carries one exploded web application and the three deployment
# descriptors the build produces (keycloak, github, none); TPL_SECURITY_MODE
# selects one at start-up. Nothing confidential is in the image: the security
# configuration is either mounted (TPL_SECURITY_CONFIG names the file) or
# written here from environment variables, and secrets such as the GitHub
# client secret stay in the environment, named by the configuration, never
# written to a file. See README.md in the docker folder of the project.

set -eu

die() {
    echo "docker-entrypoint: $*" 1>&2
    exit 1
}

# the variables named as arguments must be set and non-empty
require() {
    missing=""
    for variable in "$@"; do
        eval "value=\${$variable:-}"
        [ -n "$value" ] || missing="$missing $variable"
    done
    [ -z "$missing" ] || die "TPL_SECURITY_MODE=$MODE needs:$missing"
}

# values are written into JSON as they are: refuse quotes and backslashes
plain() {
    for variable in "$@"; do
        eval "value=\${$variable:-}"
        case "$value" in
            *\"*|*\\*) die "$variable must not contain a quote or a backslash" ;;
        esac
    done
}

WEBAPP_DIR=${TPL_WEBAPP_DIR:?TPL_WEBAPP_DIR is set by the Dockerfile}
CONTEXT=${TPL_CONTEXT:?TPL_CONTEXT is set by the Dockerfile}
DESCRIPTORS=/opt/provtemplates/security
GENERATED_CONFIG=/opt/provtemplates/run/security-config.json
MODE=${TPL_SECURITY_MODE:-keycloak}

mounted_config() {
    if [ -n "${TPL_SECURITY_CONFIG:-}" ]; then
        [ -r "$TPL_SECURITY_CONFIG" ] || die "TPL_SECURITY_CONFIG=$TPL_SECURITY_CONFIG is not a readable file"
        echo "docker-entrypoint: security configuration $TPL_SECURITY_CONFIG (mounted)"
        return 0
    fi
    return 1
}

case "$MODE" in
    keycloak)
        cp "$DESCRIPTORS/web-keycloak.xml" "$WEBAPP_DIR/WEB-INF/web.xml"
        if ! mounted_config; then
            require TPL_PUBLIC_URL KEYCLOAK_BASE_URI KEYCLOAK_REALM KEYCLOAK_CLIENT_ID KEYCLOAK_DIRECT_CLIENT_ID
            REALM_URI="$KEYCLOAK_BASE_URI/realms/$KEYCLOAK_REALM"
            KEY=${KEYCLOAK_SIGNATURE_PUBLIC_KEY:-}
            if [ -z "$KEY" ]; then
                # the realm's public signing key, a fallback for tokens without a kid
                KEY=$(curl -fsS --max-time 20 "$REALM_URI" | sed -n 's/.*"public_key" *: *"\([^"]*\)".*/\1/p') \
                    || die "cannot read the realm $REALM_URI (set KEYCLOAK_SIGNATURE_PUBLIC_KEY to start without it)"
                [ -n "$KEY" ] || die "no public_key at $REALM_URI (set KEYCLOAK_SIGNATURE_PUBLIC_KEY)"
            fi
            KEYCLOAK_SIGNATURE_PUBLIC_KEY=$KEY
            KEYCLOAK_ROLE=${KEYCLOAK_ROLE:-provwriter}
            plain TPL_PUBLIC_URL KEYCLOAK_BASE_URI KEYCLOAK_REALM KEYCLOAK_CLIENT_ID KEYCLOAK_DIRECT_CLIENT_ID KEYCLOAK_SIGNATURE_PUBLIC_KEY KEYCLOAK_ROLE
            CALLBACK="$TPL_PUBLIC_URL$CONTEXT/callback"
            cat > "$GENERATED_CONFIG" <<EOF
{
    "callbackUrl": "$CALLBACK",
    "configurations": {
        "KeycloakOidcClient": {
            "@type": "KeycloakOidcClient",
            "clientId": "$KEYCLOAK_CLIENT_ID",
            "realm": "$KEYCLOAK_REALM",
            "baseUri": "$KEYCLOAK_BASE_URI",
            "callbackUrl": "$CALLBACK"
        },
        "DirectBearerAuthClient": {
            "@type": "DirectBearerAuthClient",
            "clientId": "$KEYCLOAK_DIRECT_CLIENT_ID",
            "realm": "$KEYCLOAK_REALM",
            "baseUri": "$KEYCLOAK_BASE_URI",
            "realmUri": "$REALM_URI",
            "signaturePublicKey": "$KEYCLOAK_SIGNATURE_PUBLIC_KEY",
            "role": "$KEYCLOAK_ROLE"
        }
    }
}
EOF
            export TPL_SECURITY_CONFIG="$GENERATED_CONFIG"
            echo "docker-entrypoint: keycloak realm $REALM_URI, browser client $KEYCLOAK_CLIENT_ID, bearer client(s) $KEYCLOAK_DIRECT_CLIENT_ID, callback $CALLBACK"
        fi
        ;;
    github)
        cp "$DESCRIPTORS/web-github.xml" "$WEBAPP_DIR/WEB-INF/web.xml"
        # read by the service from the environment, whatever the configuration
        require GITHUB_CLIENT_SECRET
        [ -n "${GITHUB_ALLOWLIST+set}" ] || die "TPL_SECURITY_MODE=github needs: GITHUB_ALLOWLIST (comma-separated logins, github:<numeric id>, or *)"
        if ! mounted_config; then
            require TPL_PUBLIC_URL GITHUB_CLIENT_ID
            plain TPL_PUBLIC_URL GITHUB_CLIENT_ID
            TTL=${GITHUB_CACHE_TTL_SECONDS:-300}
            case "$TTL" in
                ''|*[!0-9]*) die "GITHUB_CACHE_TTL_SECONDS must be a number of seconds" ;;
            esac
            CALLBACK="$TPL_PUBLIC_URL$CONTEXT/callback"
            cat > "$GENERATED_CONFIG" <<EOF
{
    "callbackUrl": "$CALLBACK",
    "configurations": {
        "GitHubClient": {
            "@type": "GitHubClient",
            "clientId": "$GITHUB_CLIENT_ID",
            "clientSecretEnv": "GITHUB_CLIENT_SECRET",
            "callbackUrl": "$CALLBACK",
            "scope": "read:user",
            "allowlistEnv": "GITHUB_ALLOWLIST"
        },
        "GitHubBearerAuthClient": {
            "@type": "GitHubBearerAuthClient",
            "cacheTtlSeconds": $TTL,
            "allowlistEnv": "GITHUB_ALLOWLIST"
        }
    }
}
EOF
            export TPL_SECURITY_CONFIG="$GENERATED_CONFIG"
            echo "docker-entrypoint: github OAuth App $GITHUB_CLIENT_ID, callback $CALLBACK (must be the App's authorization callback URL)"
        fi
        ;;
    none)
        cp "$DESCRIPTORS/web-none.xml" "$WEBAPP_DIR/WEB-INF/web.xml"
        unset TPL_SECURITY_CONFIG
        echo "docker-entrypoint: WARNING TPL_SECURITY_MODE=none, every endpoint is open to anyone who can reach the port" 1>&2
        ;;
    *)
        die "TPL_SECURITY_MODE must be keycloak, github or none (was '$MODE')"
        ;;
esac

echo "docker-entrypoint: security mode $MODE, context $CONTEXT"

if [ "$#" -eq 0 ]; then
    set -- catalina.sh run
fi
exec "$@"
