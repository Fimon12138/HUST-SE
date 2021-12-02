#!/bin/bash

image_name=$1

image_version=$2

ip=$3

port=$4

profiles=$5

# 部署
if [ "${ip}" == '121.43.141.217' ]; then
  echo "----- remote deploy -----"
  user='root'
  echo "----- machine info: ip: ${ip}, user: ${user} -----"
  ssh ${user}@${ip} 'bash  -s' < DeployClient.sh "${image_name}" "${image_version}" "${ip}" "${port}" "${profiles}"
else
  echo "----- deploy to localhost -----"
  bash DeployClient.sh "${image_name}" "${image_version}" "${ip}" "${port}" "${profiles}"
fi
