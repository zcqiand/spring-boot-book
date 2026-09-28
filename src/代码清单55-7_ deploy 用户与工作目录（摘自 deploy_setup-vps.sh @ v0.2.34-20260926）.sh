# ── 2. deploy 用户（无密码、SSH key only）─────────
if ! id deploy >/dev/null 2>&1; then
  log "create deploy user"
  adduser --disabled-password --gecos "" --shell /bin/bash deploy
fi
log "ensure deploy in docker group"
usermod -aG docker deploy

# ── 3. 部署目录 ───────────────────────────────────
# springboot 用 PostgreSQL 远程, 容器内不需要 data/ 卷。只建工作目录即可。
log "create $BASE"
sudo -u deploy mkdir -p "$BASE"