#!/usr/bin/env sh

# Following: https://cloud.google.com/community/tutorials/kotlin-springboot-compute-engine

# Set the metadata server to the get project id
PROJECTID=$(curl -s "http://metadata.google.internal/computeMetadata/v1/project/project-id" -H "Metadata-Flavor: Google")
BUCKET=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/BUCKET" -H "Metadata-Flavor: Google")
JARFILE=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/JARFILE" -H "Metadata-Flavor: Google")

echo "Project ID: ${PROJECTID} Bucket: ${BUCKET}"

# Get the files we need
gsutil cp -r gs://"${BUCKET}"/"${JARFILE}" ./website-backend-builds/

# Install dependencies
apt-get update
#apt-get -y --force-yes install openjdk-17-jdk

# Make Java 8 default
#update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/jre/bin/java

# Start server
export spring_profiles_active=prod
#java -jar /website-backend-builds/"${JARFILE}"