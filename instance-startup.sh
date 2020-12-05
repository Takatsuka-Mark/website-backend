#!/usr/bin/env sh

# Following: https://cloud.google.com/community/tutorials/kotlin-springboot-compute-engine

# Set the metadata server to the get project id
PROJECTID=$(curl -s "http://metadata.google.internal/computeMetadata/v1/project/project-id" -H "Metadata-Flavor: Google")
BUCKET=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/BUCKET" -H "Metadata-Flavor: Google")

echo "Project ID: ${PROJECTID} Bucket: ${BUCKET}"

# Get the files we need
gsutil cp -r gs://"${BUCKET}"/build001.jar .

# Install dependencies
apt-get update
apt-get -y --force-yes install openjdk-11-jdk

# Make Java 8 default
update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/jre/bin/java

# Start server
java -jar build001.jar