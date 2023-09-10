#!/bin/bash

export JAR_FILE=build/libs/DataSerpro-1.0-SNAPSHOT.jar

# for arg in "$@"; do
  # echo "Processando: $arg"
  # # Seu código aqui
# done

java -jar "$JAR_FILE" "$@"

