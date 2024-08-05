#!/bin/bash

# adapted from https://medium.com/@rishabhsvats/understanding-authorization-code-flow-3946d746407

PROV_CREDENTIALS="$HOME/.kclprov"
source $PROV_CREDENTIALS


init()
{
KEYCLOAK_URL="$TPL_KEYCLOAK_BASEURI"
REDIRECT_URL="http://localhost:7072/ptl/callback"
USERNAME="$TPL_KEYCLOAK_USERNAME"
REALM="$TPL_KEYCLOAK_REALM"
CLIENTID="$TPL_KEYCLOAK_CLIENTID_INDIRECT"
}

decode() {
  jq -R 'split(".") | .[1] | @base64d | fromjson' <<< $1
}

if [[ $TPL_KEYCLOAK_PASSWORD = '' ]]; then
  echo -n Password:
  read -s PASSWORD
else
        PASSWORD=$TPL_KEYCLOAK_PASSWORD
fi


echo " "


init

COOKIE="`pwd`/target/cookie.jar.txt"


AUTHENTICATE_URL=$(curl -sSL  --get --cookie "$COOKIE" --cookie-jar "$COOKIE" \
  --data-urlencode "client_id=${CLIENTID}" \
  --data-urlencode "redirect_uri=${REDIRECT_URL}" \
  --data-urlencode "scope=openid" \
  --data-urlencode "response_type=code" \
  "$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/auth" | pup "form#kc-form-login attr{action}")

AUTHENTICATE_URL=`echo $AUTHENTICATE_URL  | sed -e 's/\&amp;/\&/g'`

echo "Sending Username Password to following authentication URL of keycloak : $AUTHENTICATE_URL"

CODE_URL=$(curl -sS --cookie "$COOKIE" --cookie-jar "$COOKIE" \
  --data-urlencode "username=$USERNAME" \
  --data-urlencode "password=$PASSWORD" \
    --write-out "%{REDIRECT_URL}" \
  "$AUTHENTICATE_URL")

echo "Following URL with code received from Keycloak : $CODE_URL"

code=`echo $CODE_URL | awk -F "code=" '{print $2}' | awk '{print $1}'`

echo "Extracted code : $code"

echo "Sending code=$code to Keycloak to receive Access token"

ACCESS_TOKEN=$(curl -sS --cookie "$COOKIE" --cookie-jar "$COOKIE" \
  --data-urlencode "client_id=$CLIENTID" \
  --data-urlencode "redirect_uri=$REDIRECT_URL" \
  --data-urlencode "code=$code" \
  --data-urlencode "grant_type=authorization_code" \
  "$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token" \
  | jq -r ".access_token")

#echo "access token : $ACCESS_TOKEN"
echo " "

echo "Decoded Access Token: "
decode $ACCESS_TOKEN
