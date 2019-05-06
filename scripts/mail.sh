#!/bin/bash

$file=../sample/build/outputs/apk/debug/*.apk

curl -n --ssl-reqd --mail-from "bimapap2017@gmail.com" \
--mail-rcpt "andatirodgers@gmail.com" -T - --url smtp://smtp.gmail.com:587 \
--user "bimapap2017@gmail.com:bimapap123" \
<<< echo "$file has been created. Whoohooo!"

