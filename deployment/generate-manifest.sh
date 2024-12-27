#!/bin/bash

# 환경 변수 설정
export userid=unicorn
export app_namespace=${userid}
export db_namespace=${userid}
export usage_image_path=unicorncr.azurecr.io/telecom/usage:latest
export alert_image_path=unicorncr.azurecr.io/telecom/alert:latest
export resources_requests_cpu=250m
export resources_requests_memory=512Mi  
export resources_limits_cpu=500m
export resources_limits_memory=1024Mi

# MongoDB 패스워드 base64 인코딩
export mongodb_password_base64=$(echo -n "Passw0rd" | base64)

# Event Grid 정보 base64 인코딩
EG_ENDPOINT=$(az eventgrid topic show --name ${userid}-pubsub-usage -g tiu-dgga-rg --query "endpoint" -o tsv)
EG_KEY=$(az eventgrid topic key list --name ${userid}-pubsub-usage -g tiu-dgga-rg --query "key1" -o tsv)

export event_grid_endpoint_base64=$(echo -n "$EG_ENDPOINT" | base64)
export event_grid_key_base64=$(echo -n "$EG_KEY" | base64)

# manifest.yaml 생성
envsubst < deployment/manifest.yaml.template > deployment/manifest.yaml
