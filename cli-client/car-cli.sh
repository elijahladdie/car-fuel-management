#!/bin/bash
# Car Management CLI Wrapper Script
# Makes it easier to run CLI commands

JAR_FILE="target/car-cli.jar"
CLI_DIR="$(dirname "$0")"

# Check if JAR exists
if [ ! -f "$CLI_DIR/$JAR_FILE" ]; then
    echo "Error: CLI JAR not found at $CLI_DIR/$JAR_FILE"
    echo "Please run 'mvn clean package' first"
    exit 1
fi

# Execute CLI with all arguments
java -jar "$CLI_DIR/$JAR_FILE" "$@"
