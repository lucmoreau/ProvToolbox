# The template service in Docker

This folder builds the Docker image of the template service, runs it with its
databases through docker compose, and tests its security.

Nothing confidential is built into the image or kept in this folder's tracked
files. The security mode, the identity provider, the database passwords and any
client secret are supplied when the containers start: from the environment, or
from `docker.conf/.env`, a file you create from `docker.conf/env.example` and
never commit (it is git-ignored).

Below, `<context>` is the service's context path, chosen when the project was
generated (`serviceContext`, for instance `/book`); `make help` prints it in the
`TPL_CONTEXT` line of the Makefile.

## Quick start

```bash
cd ..; mvn install; cd -          # the service's WARs, into the local repository
make image                        # the image for this machine
cp docker.conf/env.example docker.conf/.env
chmod 600 docker.conf/.env        # then fill it in, see below
                                  # (or source a shell file, see below)
make config.check                 # says what is missing
make docker.up                    # http://localhost:8080<context>/
```

The image includes the lexicon the explanations need (LexAccess 2013, in
`/etc/dictionary`, named by `XPLAIN_LEXICON`). The first `make image` downloads
it from the National Library of Medicine (about 200 MB, checksum verified) and
caches it in `dictionary/` (git-ignored); later builds use the cache. To use an
existing cache instead, holding `lexAccess2013.data.zip` and
`lexAccess2013.script`:

```bash
mvn install -Ddocker.make.target=build.native -Dcached.dictionary.folder=/path/to/dictionary
```

`make docker.ps`, `make docker.logs`, `make docker.restart`, `make docker.stop`,
`make docker.rm` and `make docker.down` manage the running stack (compose
project `tpl`).

## The three security modes

The service build produces three WARs that differ only by their deployment
descriptor `WEB-INF/web.xml`:

| WAR classifier | descriptor (service module)       | security                                     | `TPL_SECURITY_MODE` |
|----------------|-----------------------------------|----------------------------------------------|---------------------|
| `secure`       | `web-with-security.xml`           | Keycloak: browser login (OIDC) and bearer tokens | `keycloak` (default) |
| `github`       | `web-with-github-security.xml`    | GitHub: browser login (OAuth App) and bearer tokens, restricted to an allowlist | `github` |
| `unsecure`     | `web-no-security.xml`             | none: every endpoint open                    | `none`              |

When the service runs under Jetty (`make jetty`, `make jetty.github`,
`make jetty.nosec` in the service folder), the Maven profile picks the
descriptor: `security`, `github` or `nosecurity`.

The image carries one web application, unpacked from the `secure` WAR, and the
three descriptors (in `/opt/provtemplates/security`). At start-up the entry point
installs the descriptor that `TPL_SECURITY_MODE` names, so a single image serves
the three modes. An unknown mode, or a mode whose configuration is incomplete,
stops the container with a message saying what is missing (`make docker.logs`).

In both secured modes the filters protect `<context>/provapi/*`,
`<context>/forms.html`, `<context>/expander.html` and `<context>/api.html`. A
request without valid credentials is redirected to the identity provider's
login page; a script sends `Authorization: Bearer <token>` instead. The other
pages (`<context>/`, the icons, the webjars) are public.

## Configuration

Every value below goes in `docker.conf/.env` or in the environment of `make`;
the environment wins. `make config.check` checks the mode of `TPL_SECURITY_MODE`
(`make config.check MODE=github` checks another one) and prints `ok`,
`MISSING` or `WRONG` for each value, never the value of a secret.

### In a shell file instead of `.env`

The same values can live in a shell file outside the project, which also holds
what `make token.keycloak` and `make token.github` need (see
[Bearer tokens](#bearer-tokens)), and which every `make` started from that shell
then sees. The file must be *sourced*, not executed: run as a command, its
`export`s vanish with the shell that ran it.

```bash
# ~/.docker.credentials.sh, readable only by you (chmod 600)
# load it with: source ~/.docker.credentials.sh

# service
export TPL_SECURITY_MODE=keycloak          # or github, or none
export TPL_HTTP_PORT=8080
export TPL_PUBLIC_URL=http://localhost:8080

# database
export POSTGRES_PASSWORD=...
export TPL_DB_USER=...
export TPL_DB_PASS=...

# keycloak mode
export KEYCLOAK_BASE_URI=https://keycloak.example.org/auth
export KEYCLOAK_REALM=prov
export KEYCLOAK_CLIENT_ID=...              # browser login (public client)
export KEYCLOAK_DIRECT_CLIENT_ID=...       # bearer tokens

# github mode
export GITHUB_CLIENT_ID=...
export GITHUB_CLIENT_SECRET=...
export GITHUB_ALLOWLIST=...                # logins, github:<numeric id>, or *

# make token.keycloak (the service's bin/keycloak-get-token.sh)
export TPL_KEYCLOAK=$KEYCLOAK_BASE_URI/realms/$KEYCLOAK_REALM/protocol/openid-connect/token
export TPL_KEYCLOAK_CLIENTID_DIRECT=$KEYCLOAK_DIRECT_CLIENT_ID
export TPL_KEYCLOAK_USERNAME=...
# TPL_KEYCLOAK_PASSWORD: leave it unset to be asked for the password

# make token.github (the service's bin/github-get-token.sh): either
export TPL_GITHUB_TOKEN=...                # a personal access token, or
# export TPL_GITHUB_CLIENTID=...           # an OAuth App with Device Flow enabled
```

Then:

```bash
source ~/.docker.credentials.sh
make config.check
make docker.up
```

The names are those of the table below: `KEYCLOAK_BASE_URI`, not
`KEYCLOAK_BASEURI`; `KEYCLOAK_DIRECT_CLIENT_ID`, not
`TPL_KEYCLOAK_CLIENTID_DIRECT`, which only the token script reads. A value set
in the environment wins over `docker.conf/.env`, so the two can be combined
(for instance the secrets in the shell file, the rest in `.env`); remove a
variable from the shell (`unset NAME`) to fall back on `.env`.

### Service and ports

| variable            | meaning                                                                 | default |
|---------------------|-------------------------------------------------------------------------|---------|
| `TPL_SECURITY_MODE` | `keycloak`, `github` or `none`                                          | `keycloak` |
| `TPL_HTTP_PORT`     | port of the host on which the service listens (the container's is 8080) | `8080` |
| `TPL_PUBLIC_URL`    | URL at which browsers reach the service, without `<context>`; the login callback is `TPL_PUBLIC_URL<context>/callback`, and must be registered with the identity provider | `http://localhost:TPL_HTTP_PORT` |
| `TPL_IMAGE`         | image to run                                                            | the one `make image` builds for this machine |
| `TPL_SECURITY_CONFIG` | path, inside the container, of a security configuration file you mount yourself; when set, the Keycloak or GitHub variables below are not used (the GitHub client secret and allowlist still are) | empty |

Behind a reverse proxy that terminates TLS, set `TPL_PUBLIC_URL` to the public
`https://` URL.

### Database

The stack runs PostgreSQL 16 (templates), Redis and MongoDB (documents). Only
PostgreSQL needs credentials:

| variable            | meaning |
|---------------------|---------|
| `POSTGRES_PASSWORD` | password of the `postgres` superuser of the container; used on the first start, on an empty data folder, to create the `templates` database and the service's account |
| `TPL_DB_USER`       | the service's database account |
| `TPL_DB_PASS`       | its password |

The data live in `docker.conf/deploy/` (git-ignored); `TPL_DATA_DIR` moves them.
PostgreSQL creates the accounts only on the first start of an empty data
folder. If that first start failed, or ran with other values, the service
then reports `password authentication failed`: either empty
`docker.conf/deploy/postgres16/data` (losing the records), or apply the current
`TPL_DB_USER` and `TPL_DB_PASS` to the existing database:

```bash
docker exec tpl-postgres16-1 sh /docker-entrypoint-initdb.d/01-init.sh
```

The same holds for MongoDB: data written by another version of the `mongo`
image make it restart in a loop (`make docker.ps`); empty
`docker.conf/deploy/mongo` to start afresh.

`docker.conf/config/` holds the service's non-confidential configuration,
mounted read-only: `template-config.json` (template catalogue, JDBC URL of the
`postgres16` container, explanation plans) and `storage-configuration.json`
(MongoDB and Redis).

### Keycloak (`TPL_SECURITY_MODE=keycloak`)

| variable                        | meaning |
|---------------------------------|---------|
| `KEYCLOAK_BASE_URI`             | Keycloak's base URL, before `/realms`, for example `https://keycloak.example.org/auth` |
| `KEYCLOAK_REALM`                | the realm |
| `KEYCLOAK_CLIENT_ID`            | the client of the browser login: an OpenID Connect client with *Client authentication* off (public) and *Standard flow* on, whose *Valid redirect URIs* include `TPL_PUBLIC_URL<context>/callback` |
| `KEYCLOAK_DIRECT_CLIENT_ID`     | the client(s), comma-separated, whose access tokens the service accepts as bearer tokens: a token is accepted when its signature verifies against the realm's keys, its issuer is `KEYCLOAK_BASE_URI/realms/KEYCLOAK_REALM` and its `azp` is one of these clients |
| `KEYCLOAK_SIGNATURE_PUBLIC_KEY` | optional: the realm's RSA public key (realm settings, *Keys*). When empty, the entry point reads it from the realm at start-up. The service otherwise verifies signatures against the realm's published keys, so this key only serves tokens without a key id |
| `KEYCLOAK_ROLE`                 | role given to users authenticated by bearer token (default `provwriter`) |

Keycloak has no client secret here: both clients are public. The container must
reach `KEYCLOAK_BASE_URI`, both at start-up and to verify tokens.

### GitHub (`TPL_SECURITY_MODE=github`)

Register an OAuth App (GitHub, *Settings*, *Developer settings*, *OAuth Apps*,
*New OAuth App*) with *Authorization callback URL*
`TPL_PUBLIC_URL<context>/callback`. GitHub accepts only that exact URL, so one
OAuth App serves one public URL.

| variable                   | meaning |
|----------------------------|---------|
| `GITHUB_CLIENT_ID`         | the OAuth App's client id |
| `GITHUB_CLIENT_SECRET`     | a client secret of the OAuth App (*Generate a new client secret*). It stays in the container's environment; the security configuration only names the variable |
| `GITHUB_ALLOWLIST`         | who may use the service: comma-separated GitHub logins, `github:<numeric id>`, or `*` for any GitHub user. Must be set; empty admits nobody |
| `GITHUB_CACHE_TTL_SECONDS` | how long a bearer token checked against GitHub stays accepted (default 300); a revoked token can be accepted that long |

Scripts send a GitHub token as `Authorization: Bearer <token>`: the service asks
GitHub whose token it is and admits the user if allowlisted. Use a fine-grained
personal access token with no permission (see `bin/github-get-token.sh` of the
service).

### Mounting your own security configuration

Instead of the Keycloak or GitHub variables, you may mount a security
configuration file, such as the examples in the service's
`src/test/config/security/`, and name it:

```yaml
    volumes:
      - ./my-security-config.json:/home/pservice/security-config.json:ro
```

with `TPL_SECURITY_CONFIG=/home/pservice/security-config.json`. A client secret
never goes in that file: `GitHubClient` names the variable that holds it
(`clientSecretEnv`), as it names the allowlist's (`allowlistEnv`).

### Where the secrets end up

Values given as environment variables are visible to whoever can run
`docker inspect` on the container: restrict access to the Docker daemon. They
are not in the image, not in the logs (the security tests check both), and not
on the Java command line (the database credentials are passed as environment
variables, not as `-D` options).

## Security tests

```bash
make test.keycloak      # needs the Keycloak values and a fresh access token
make test.github        # needs the GitHub values and a GitHub token
make test.security      # both
```

Each test first runs `make config.check.keycloak` (or `.github`), which checks
the mode's configuration, the image, `curl`, `jq` and the bearer token, then
starts its own stack (project `tpl-security-test-<mode>`, port `TEST_PORT`,
18080 by default, data in `target/security-test/`), so it does not disturb a
running deployment, and removes it afterwards (`KEEP=true` leaves it running;
`make test.down MODE=<mode>` removes it). It checks that:

- without credentials, `POST <context>/provapi/templates/records` (the
  dashboard's record search), `POST <context>/provapi/statements` and
  `GET <context>/forms.html` are
  refused or redirected to the identity provider's login, with the configured
  client id, while `GET <context>/` is public;
- a malformed bearer token is refused, and so is (Keycloak) a forged token, the
  valid token's claims with another signature;
- the valid bearer token of `TOKEN_FILE` is accepted: the record search and
  `forms.html` answer 200; `POST provapi/statements` must answer 2xx when
  `STATEMENT_FILE` names a JSON statement valid for the project's catalogue
  (it is then recorded in the test's database), and otherwise must only get
  past the security filter, since an empty list is not a valid statement;
- the database and client secrets appear neither in the image nor in the logs;
- the same valid token is refused once the service no longer trusts it: the
  container restarted with another `KEYCLOAK_DIRECT_CLIENT_ID`, or with a
  `GITHUB_ALLOWLIST` that does not name the token's user.

The record search is for `BASE_RELATION`, by default the first base relation
the database knows (`search_records_for_<relation>`).

### Bearer tokens

`make token.keycloak` and `make token.github` run the service's scripts
`../<project>-service/bin/keycloak-get-token.sh` and `github-get-token.sh`,
which write `~/.keycloak_token` and `~/.github_token` (the tests' default
`TOKEN_FILE`s). They read their settings from the environment (see the shell
file above) and then from `~/.openprovenance.credentials.sh`, whose values,
when that file exists, win:

- Keycloak: `TPL_KEYCLOAK`, the realm's token endpoint
  (`KEYCLOAK_BASE_URI/realms/KEYCLOAK_REALM/protocol/openid-connect/token`);
  `TPL_KEYCLOAK_CLIENTID_DIRECT`, a client among `KEYCLOAK_DIRECT_CLIENT_ID`
  with *Direct access grants* on; `TPL_KEYCLOAK_USERNAME`; and
  `TPL_KEYCLOAK_PASSWORD`, asked for when unset.
- GitHub: `TPL_GITHUB_TOKEN`, a personal access token stored as it is, or else
  `TPL_GITHUB_CLIENTID`, an OAuth App with *Device Flow* enabled: the script
  prints a code to enter at github.com/login/device.

A Keycloak token lasts minutes, so get it just before the test:

```bash
make token.keycloak test.keycloak
make token.github test.github
```

The bearer token:

- Keycloak: `TOKEN_FILE`, by default `~/.keycloak_token`, holds an access token
  of a `KEYCLOAK_DIRECT_CLIENT_ID` client, for instance as written by the
  service's `bin/keycloak-get-token.sh`. Keycloak tokens last minutes: get a
  fresh one just before the test.
- GitHub: `TOKEN_FILE`, by default `~/.github_token`, holds a GitHub token of a
  user named in `GITHUB_ALLOWLIST` (`bin/github-get-token.sh`).

The tests do not log in through a browser; the redirect to the login page is
checked, the login itself is not.
