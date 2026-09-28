if [ ! -f "$BASE/springboot.env" ]; then
  if [ -n "${DATABASE_URL:-}" ] && [ -n "${DATABASE_USER:-}" ] && [ -n "${DATABASE_PASSWORD:-}" ] && [ -n "${JWT_SIGNING_KEY:-}" ] && [ -n "${LAB_SAAS_CLIENT_SECRET:-}" ]; then
    echo "→ bootstrapping $BASE/springboot.env from env DATABASE_URL/USER/PASSWORD + JWT_SIGNING_KEY + LAB_SAAS_CLIENT_SECRET"
    umask 077
    {
      printf 'DATABASE_URL=%s\n' "$DATABASE_URL"
      printf 'DATABASE_USER=%s\n' "$DATABASE_USER"
      printf 'DATABASE_PASSWORD=%s\n' "$DATABASE_PASSWORD"
      printf 'SERVER_PORT=5205\n'
      printf 'JWT_SIGNING_KEY=%s\n' "$JWT_SIGNING_KEY"
      printf 'LAB_SAAS_CLIENT_SECRET=%s\n' "$LAB_SAAS_CLIENT_SECRET"
    } > "$BASE/springboot.env"
    chmod 600 "$BASE/springboot.env"
  else
    echo "ERROR: $BASE/springboot.env missing. Set DATABASE_URL/USER/PASSWORD + JWT_SIGNING_KEY + LAB_SAAS_CLIENT_SECRET ..." >&2
    exit 1
  fi
fi
if ! grep -q '^DATABASE_URL=' "$BASE/springboot.env"; then
  echo "ERROR: $BASE/springboot.env has no DATABASE_URL line (old key LAB_DATABASE_URL?)" >&2
  exit 1
fi
if ! grep -q '^JWT_SIGNING_KEY=' "$BASE/springboot.env"; then
  echo "ERROR: $BASE/springboot.env has no JWT_SIGNING_KEY line (old key LAB_JWT_SECRET?)" >&2
  exit 1
fi