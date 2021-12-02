#!/bin/bash

REGISTRY_URL="registry.cn-hangzhou.aliyuncs.com"
REGISTRY_NAMESPACE="fimon"
REGISTRY_USERNAME="fimonlu"
REGISTRY_PASSWORD="Lyh990527"

image_name=$1

version_id=$2

echo "push image: ${image_name}:${version_id}"

echo "----- login to remote registry -----"
docker login --username=${REGISTRY_USERNAME} --password=${REGISTRY_PASSWORD}  ${REGISTRY_URL}

new_image_id_array=($(docker images|grep "${image_name}"|grep "${version_id}"|awk '{print $3}'))
new_image_id=${new_image_id_array[0]}
echo "----- new image id: ${new_image_id} -----"

echo "----- tag image -----"
docker tag "${new_image_id}" ${REGISTRY_URL}/${REGISTRY_NAMESPACE}/"${image_name}:${version_id}"

echo "----- push image -----"
docker push ${REGISTRY_URL}/${REGISTRY_NAMESPACE}/"${image_name}:${version_id}"