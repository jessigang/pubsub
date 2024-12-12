#!/bin/bash

# Define the crew IDs
START=100
END=100
DOMAIN_SUFFIX="4.217.249.140.nip.io"
NGINX_SITES_AVAILABLE="/etc/nginx/sites-available"
NGINX_SITES_ENABLED="/etc/nginx/sites-enabled"

for i in $(seq $START $END)
do
    CREW_ID="crew${i}"
    DOMAIN="${CREW_ID}.${DOMAIN_SUFFIX}"

    echo "Creating certificate for ${DOMAIN}..."
    #sudo certbot --nginx -d $DOMAIN

    CONFIG_FILE="${NGINX_SITES_AVAILABLE}/${CREW_ID}"

    echo "Creating Nginx configuration for ${DOMAIN} at ${CONFIG_FILE}..."
    sudo bash -c "cat > ${CONFIG_FILE}" <<EOL
server {
    listen 443 ssl;
    server_name ${DOMAIN};
    ssl_certificate /etc/letsencrypt/live/${DOMAIN}/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/${DOMAIN}/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    location / {
        #proxy_pass http://20.214.113.85:80;
        proxy_ssl_verify off;
        proxy_buffer_size 64k;
        proxy_buffers 4 64k;
        proxy_busy_buffers_size 64k;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 60s;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
    }
}
EOL

    echo "Creating symbolic link for ${CREW_ID}..."
    sudo ln -sf ${CONFIG_FILE} ${NGINX_SITES_ENABLED}/${CREW_ID}

done

# Reload Nginx to apply changes
echo "Reloading Nginx..."
sudo systemctl reload nginx

echo "All configurations and certificates are complete."

