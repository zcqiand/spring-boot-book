NGINX_SITES_AVAILABLE="/etc/nginx/sites-available"
NGINX_SITES_ENABLED="/etc/nginx/sites-enabled"
NGINX_VHOST_FILE="${NGINX_SITES_AVAILABLE}/${NGINX_DOMAIN}"
NGINX_VHOST_LINK="${NGINX_SITES_ENABLED}/${NGINX_DOMAIN}"
NGINX_TEMPLATE="${BASE}/nginx-vps.conf.example"

# 拉模板:每次都从 master 拉最新 —— VPS 本地会留 7 月老模板(801x 端口时代),
# fetch-if-missing 让它永不更新 → 渲染出 proxy_pass 8010 全家族 502
# (2026-09-03 事故根因之二;set -eu 下 curl 失败即 fail-fast)
echo "→ fetching nginx-vps.conf.example template (always fresh from master)"
curl -fsSL "https://raw.githubusercontent.com/zcqiand/saas-identity-platform-springboot/refs/heads/master/deploy/nginx-vps.conf.example" -o "${NGINX_TEMPLATE}"

# 渲染到临时文件 —— sed 同时覆盖 3 种 placeholder:
#   Style A (lab-vue/react):      <domain>
#   Style B/C (nextjs/sp/aspc):   lab.YOUR_DOMAIN / saas.YOUR_DOMAIN
#   cert 路径: your-cert.{crt,cert} / <domain>.crt → 统一到 ${NGINX_CERT_BASENAME}.cert
TMP_VHOST="$(mktemp -t vpstpl.XXXXXX)"
# cert 归一化规则必须排在 <domain>/YOUR_DOMAIN 通配之前:sed -e 按顺序执行,
# 先替换 <domain> 会把 cert 路径里的占位符一并吃掉,后面的 cert 规则全部失配
# (2026-09-03 VPS nginx -t "cannot load certificate <域名>.crt" 事故根因)
sed \
  -e "s|/etc/nginx/ssl/<domain>\.crt|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.cert|g" \
  -e "s|/etc/nginx/ssl/<domain>\.cert|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.cert|g" \
  -e "s|/etc/nginx/ssl/<domain>\.key|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.key|g" \
  -e "s|/etc/nginx/ssl/your-cert\.crt|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.cert|g" \
  -e "s|/etc/nginx/ssl/your-cert\.cert|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.cert|g" \
  -e "s|/etc/nginx/ssl/your-cert\.key|/etc/nginx/ssl/${NGINX_CERT_BASENAME}.key|g" \
  -e "s|<domain>|${NGINX_DOMAIN}|g" \
  -e "s|lab\.YOUR_DOMAIN|${NGINX_DOMAIN}|g" \
  -e "s|saas\.YOUR_DOMAIN|${NGINX_DOMAIN}|g" \
  "${NGINX_TEMPLATE}" > "${TMP_VHOST}"

# diff 检测:已有 vhost 且内容相同就 skip,不同才重写 + reload
if [ -e "${NGINX_VHOST_FILE}" ] && diff -q "${TMP_VHOST}" "${NGINX_VHOST_FILE}" >/dev/null 2>&1; then
  echo "→ nginx vhost ${NGINX_VHOST_FILE} unchanged, skip"
  rm -f "${TMP_VHOST}"