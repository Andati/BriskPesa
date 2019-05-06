#!/bin/bash

#file=../sample/build/outputs/apk/debug/*.apk
file=`pwd`

curl -n --ssl-reqd --mail-from "bimapap2017@gmail.com" \
--mail-rcpt "andatirodgers@gmail.com" -T - --url smtps://smtp.gmail.com:465 \
--user "bimapap2017@gmail.com:bimapap123" --insecure \
<<<`echo "$file has been created! Hurray!!"`

