#!/bin/bash

# 镜像仓库
REGISTRY_URL='registry.cn-hangzhou.aliyuncs.com'
REGISTRY_NAMESPACE='fimon'
REGISTRY_USERNAME='fimonlu'
REGISTRY_PASSWORD='Lyh990527'

image_name=$1

image_version=$2

ip=$3

port=$4

profiles=$5

if [ "${image_name}" == 'melon-gateway' ]; then
  inner_port=88
elif [ "${image_name}" == 'fdfs-cli' ]; then
  inner_port=8080
elif [ "${image_name}" == 'melon-user' ]; then
  inner_port=8081
elif [ "${image_name}" == 'melon-activity' ]; then
  inner_port=8082
else
  inner_port=8083
fi

echo "----- docker registry login -----"
docker login --username=${REGISTRY_USERNAME} --password=${REGISTRY_PASSWORD} ${REGISTRY_URL}

echo "----- pull image: name: ${image_name}, version: ${image_version} -----"
docker pull ${REGISTRY_URL}/${REGISTRY_NAMESPACE}/"${image_name}":"${image_version}"

existing_docker_id=$(docker ps -a|grep "${image_name}"|awk '{print $1}')
if [ -n "${existing_docker_id}" ]; then
   echo "----- remove existing docker: ${existing_docker_id} -----"
   docker stop "${existing_docker_id}"
   docker rm -f "${existing_docker_id}"
fi

echo "----- run new docker -----"
docker run -d --name="${image_name}" -p "${port}":"${inner_port}" \
-e IP="${ip}" -e PORT="${port}" -e PROFILES="${profiles}" \
--restart=always "${image_name}":"${image_version}"