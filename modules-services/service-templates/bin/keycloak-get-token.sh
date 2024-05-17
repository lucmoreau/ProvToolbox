#!/bin/bash                                                                                                                                                                           

PROV_CREDENTIALS="$HOME/.kclprov"
TOKEN_DESTINATION="$HOME/.keycloak_token"
VERBOSE=true

if [ $# -ne 0 ]; then
  echo 1>&2 "Usage: . $0  "
  echo 1>&2 "   gets a token for username (specified as TPL_KEYCLOAK_USERNAME in $PROV_CREDENTIALS) and stores it in $TOKEN_DESTINATION "
  exit
fi


source $PROV_CREDENTIALS

CLIENT_ID="KeycloakOidcClient"
CLIENT_ID="assistant"
CLIENT_ID="ptm"


if [[ $TPL_KEYCLOAK_PASSWORD = '' ]]; then
  echo -n Password:
  read -s PASSWORD
else
        PASSWORD=$TPL_KEYCLOAK_PASSWORD
fi

LINE="curl -k -s -X POST $TPL_KEYCLOAK  -d username=$TPL_KEYCLOAK_USERNAME -d password=$PASSWORD -d grant_type=password -d client_id=$CLIENT_ID"

if [[ $VERBOSE == true ]]; then
        echo $LINE
fi

TOKEN=$($LINE | jq -r '.access_token')

if [[ $(echo $TOKEN) != 'null' ]]; then
        echo $TOKEN > $TOKEN_DESTINATION
        echo "Token saved in $TOKEN_DESTINATION"
fi


#echo $TOKEN


