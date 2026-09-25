#!/bin/bash

PROV_CREDENTIALS="$HOME/.openprovenance.credentials.sh"
TOKEN_DESTINATION="$HOME/.github_token"
VERBOSE=false

if [ $# -ne 0 ]; then
  echo 1>&2 "Usage: $0  "
  echo 1>&2 "   gets a GitHub token, to be sent as a Bearer token to a service deployed with web-with-github-security.xml, and stores it in $TOKEN_DESTINATION"
  echo 1>&2 "   if TPL_GITHUB_TOKEN (a personal access token) is set in $PROV_CREDENTIALS, it is stored as is;"
  echo 1>&2 "   otherwise the OAuth device flow is run for the OAuth App TPL_GITHUB_CLIENTID (Device Flow must be enabled in the App's settings)"
  exit
fi


source $PROV_CREDENTIALS


if [[ $TPL_GITHUB_TOKEN != '' ]]; then
        TOKEN=$TPL_GITHUB_TOKEN
else
        if [[ $TPL_GITHUB_CLIENTID = '' ]]; then
          echo 1>&2 "Neither TPL_GITHUB_TOKEN nor TPL_GITHUB_CLIENTID is set in $PROV_CREDENTIALS"
          exit 1
        fi

        DEVICE=$(curl -s -X POST -H "Accept: application/json" https://github.com/login/device/code -d client_id=$TPL_GITHUB_CLIENTID)
        DEVICE_CODE=$(jq -r '.device_code' <<< "$DEVICE")
        USER_CODE=$(jq -r '.user_code' <<< "$DEVICE")
        VERIFICATION_URI=$(jq -r '.verification_uri' <<< "$DEVICE")
        INTERVAL=$(jq -r '.interval' <<< "$DEVICE")
        EXPIRES_IN=$(jq -r '.expires_in' <<< "$DEVICE")

        if [[ $DEVICE_CODE = 'null' || $DEVICE_CODE = '' ]]; then
          echo 1>&2 "Device flow refused: $DEVICE"
          exit 1
        fi

        echo 1>&2 "Open $VERIFICATION_URI and enter the code $USER_CODE"

        TOKEN=null
        DEADLINE=$(( $(date +%s) + EXPIRES_IN ))
        while [[ $(date +%s) -lt $DEADLINE ]]; do
          sleep $INTERVAL
          RESPONSE=$(curl -s -X POST -H "Accept: application/json" https://github.com/login/oauth/access_token \
                       -d client_id=$TPL_GITHUB_CLIENTID -d device_code=$DEVICE_CODE -d grant_type=urn:ietf:params:oauth:grant-type:device_code)
          ERROR=$(jq -r '.error' <<< "$RESPONSE")
          case $ERROR in
            null)                  TOKEN=$(jq -r '.access_token' <<< "$RESPONSE"); break ;;
            authorization_pending) ;;
            slow_down)             INTERVAL=$(jq -r '.interval' <<< "$RESPONSE") ;;
            *)                     echo 1>&2 "Device flow failed: $ERROR"; exit 1 ;;
          esac
        done
fi

if [[ $(echo $TOKEN) != 'null' ]]; then
        (umask 077; echo $TOKEN > $TOKEN_DESTINATION)
        #echo "Token saved in $TOKEN_DESTINATION"
        if [[ $VERBOSE == true ]]; then
          curl -s -H "Authorization: Bearer $TOKEN" https://api.github.com/user | jq '{login, id}'
        fi
else
        echo 1>&2 "No token obtained"
        exit 1
fi
