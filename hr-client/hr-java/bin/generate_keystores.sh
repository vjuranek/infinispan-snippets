#!/bin/bash

echo "Generating server certificate"
keytool -genkeypair -keystore keystore_server.jks -alias ispn_server -dname "CN=node0,O=Infinispan" -keyalg RSA -keysize 2048 -storepass secret -keypass changeme
echo "Exporting server certificate"
keytool -exportcert -keystore keystore_server.jks -alias ispn_server -storepass secret > ispn_server.cert
echo "Importing server certificate into client truststore"
keytool -import -noprompt -alias ispn_server -file ispn_server.cert -keystore truststore_client.jks -storepass secret

echo "Generating client certificate"
keytool -genkeypair -keystore keystore_client.jks -alias hr_client -dname "CN=hr-client,O=Infinispan" -keyalg RSA -keysize 2048 -storepass secret -keypass changeme
echo "Exporting client certificate"
keytool -exportcert -keystore keystore_client.jks -alias hr_client -storepass secret > hr_client.cert
echo "Importing client certificate into server truststore"
keytool -import -noprompt -alias hr_client -file hr_client.cert -keystore truststore_server.jks -storepass secret 

clear
echo "Content of generated stores:"
echo " =================="
echo "| Server keystore: |"
echo " =================="
keytool -list -keystore keystore_server.jks -storepass secret
echo "-------------------------------------------------------------------------------------------"
echo " ===================="
echo "| Server truststore: |"
echo " ===================="
keytool -list -keystore truststore_server.jks -storepass secret
echo "-------------------------------------------------------------------------------------------"
echo " =================="
echo "| Client keystore: |"
echo " =================="
keytool -list -keystore keystore_client.jks -storepass secret
echo "-------------------------------------------------------------------------------------------"
echo " ===================="
echo "| Client truststore: |"
echo " ===================="
keytool -list -keystore truststore_client.jks -storepass secret
echo "-------------------------------------------------------------------------------------------"

printf "\n\n\n"
echo "Moving stores into certs dir ..."
mkdir -p certs
mv keystore_server.jks certs/
mv truststore_server.jks certs/
mv keystore_client.jks certs/
mv truststore_client.jks certs/

echo "DONE!"
