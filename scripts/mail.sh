#!/bin/bash

apt-get install sendemail

SMTPTO=andatirodgers@gmail.com
SMTPFROM=bimapap2017@gmail.com
SMTPSERVER=smtp.gmail.com:587
SMTPUSER=bimapap2017@gmail.com
SMTPPASS=bimapap123
MESSAGE="Build is complete!"
SUBJECT="Custom Build Notification!"

sendemail -f $SMTPFROM -t $SMTPTO -u $SUBJECT -m $MESSAGE -s $SMTPSERVER -xu $SMTPUSER -xp $SMTPPASS -o tls=yes

