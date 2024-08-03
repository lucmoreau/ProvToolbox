#!/bin/bash

# adapted from https://medium.com/@rishabhsvats/understanding-authorization-code-flow-3946d746407

PROV_CREDENTIALS="$HOME/.kclprov"
source $PROV_CREDENTIALS


USERNAME="$TPL_KEYCLOAK_USERNAME"

if [[ $TPL_KEYCLOAK_PASSWORD = '' ]]; then
  echo -n Password:
  read -s PASSWORD
else
        PASSWORD=$TPL_KEYCLOAK_PASSWORD
fi




COOKIE="`pwd`/target/cookie.jar.txt"


AUTHENTICATE_URL=$(curl -s -D - --location --cookie-jar "$COOKIE" $1 | pup "form#kc-form-login attr{action}")

AUTHENTICATE_URL=`echo $AUTHENTICATE_URL  | sed -e 's/\&amp;/\&/g'`

echo "- 1. Sending Username/Password to following authentication URL of keycloak : $AUTHENTICATE_URL"

CALLBACK_URL=$(curl -s --cookie "$COOKIE" --cookie-jar "$COOKIE" \
  --data-urlencode "username=$USERNAME" \
  --data-urlencode "password=$PASSWORD" \
  --write-out "%{REDIRECT_URL}" \
  "$AUTHENTICATE_URL")

shift

echo "- 2. Following callback received from Keycloak : $CALLBACK_URL and $*"

curl -sS --cookie "$COOKIE" --cookie-jar "$COOKIE" --location "$CALLBACK_URL" $*

