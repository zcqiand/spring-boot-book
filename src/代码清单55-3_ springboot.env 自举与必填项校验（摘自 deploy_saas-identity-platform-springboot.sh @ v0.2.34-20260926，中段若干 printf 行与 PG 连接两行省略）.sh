if [ ! -f "$BASE/springboot.env" ]; then
  if [ -n "${DATABASE_URL:-}" ] && [ -n "${DATABASE_USER:-}" ] && [ -n "${DATABASE_PASSWORD:-}" ]; then
    echo "→ bootstrapping $BASE/springboot.env from env DATABASE_URL/USER/PASSWORD (key 集合 = .env.production)"
    umask 077
    {
      printf 'DATABASE_URL=%s\n' "$DATABASE_URL"
      printf 'DATABASE_USER=%s\n' "$DATABASE_USER"
      printf 'DATABASE_PASSWORD=%s\n' "$DATABASE_PASSWORD"
      printf 'DATABASE_NAME=saas_prod\n'
      printf 'SERVER_PORT=5105\n'
# 中略：PG_HOST（内网地址）与 PG_PASSWORD 等数行 printf 含内网地址与口令，见源文件，本书不录
      # JWT 三件套显式写(JwtIssuer @Value 默认值兜底是反模式,禁;值=契约文件值)
      printf 'JWT_AUTHORITY=https://auth.example.com\n'
      printf 'JWT_ISSUER=saas-identity-platform\n'
      printf 'JWT_AUDIENCE=saas-identity-platform-clients\n'
      printf 'JWT_TTL_SECONDS=3600\n'
      # JWT_SIGNING_KEY 首启随机生成,append-only 持久化(见下方 v0.2.0 段注释)
      printf 'JWT_SIGNING_KEY=%s\n' "$(head -c 48 /dev/urandom | base64 | tr -d '\n')"
    } > "$BASE/springboot.env"
    chown deploy:deploy "$BASE/springboot.env" 2>/dev/null || true
    chmod 600 "$BASE/springboot.env"
  else
    echo "ERROR: $BASE/springboot.env missing. Set DATABASE_URL/USER/PASSWORD env (e.g. DATABASE_URL=jdbc:postgresql://host/saas_prod DATABASE_USER=postgres DATABASE_PASSWORD=... sudo -E sh deploy/setup-vps.sh saas-springboot.example.com) or run setup-vps.sh first." >&2
    exit 1
  fi
fi