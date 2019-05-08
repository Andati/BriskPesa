#!/bin/bash

#file=./sample/build/outputs/apk/debug/*.apk
file=`find $directory -type f -name "*debug*.apk"`

if ! mkdir gnevo.lock; then
    exit 1
fi

# use the ** in this script at your own peril
curl -n --ssl-reqd --mail-from "bimapap2017@gmail.com" \
--mail-rcpt "andatirodgers@gmail.com" -T - --url smtps://smtp.gmail.com:465 \
--user "bimapap2017@gmail.com:bimapap123" --insecure \
<<<`echo "$file has been created! Hurray!"`

scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i gnevo/ak $file ubuntu@34.240.32.220:/home/ubuntu/CIStuff

