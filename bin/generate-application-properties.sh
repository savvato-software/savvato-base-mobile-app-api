#!/bin/bash

WORKING_DIR=$(pwd)

# Set paths
DEV_PROPERTIES="$WORKING_DIR/src/main/resources/application-dev.properties"
TARGET_PROPERTIES="$WORKING_DIR/src/main/resources/application.properties"
KEYS_FILE="$HOME/src/keys.yaml"

# Copy the dev properties to the target
cp $DEV_PROPERTIES $TARGET_PROPERTIES

# Read keys from the local file and update the playbook
while IFS= read -r line; do
    key=$(echo "$line" | cut -d '=' -f 1)
    value=$(echo "$line" | cut -d '=' -f 2-)
    sed_command="sed -i \"s/^\([[:space:]]*\)$key=.*/\1$key=$value/\" \"$TARGET_PROPERTIES\""
    # echo "Executing: $sed_command"
    eval "$sed_command"
done < <(yq e 'to_entries | .[] | "\(.key)=\(.value)"' "$KEYS_FILE")

echo "Generated application.properties with replaced values."

