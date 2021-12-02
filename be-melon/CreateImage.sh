#!/bin/bash

module_name=$1

version_id=$2

echo "----- create image: image_name: ${module_name}, image_version: ${version_id} -----"

echo "----- cd target dir -----"
cd "${module_name}"

echo "----- copy jar file -----"
cp "target/${module_name}-0.0.1-SNAPSHOT.jar" "${module_name}.jar"

echo "----- build image -----"
docker build -t "${module_name}:${version_id}" .