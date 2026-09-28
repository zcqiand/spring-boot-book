echo "→ image: $IMAGE"
echo "→ docker login"
printf '%s' "$PASSWORD" | docker login -u "$USERNAME" --password-stdin

echo "→ docker pull"
docker pull "$IMAGE"

echo "→ docker stop & rm $CONTAINER_NAME"
docker stop "$CONTAINER_NAME" 2>/dev/null || true
docker rm "$CONTAINER_NAME" 2>/dev/null || true

echo "→ docker run"
docker run -d \
  --name "$CONTAINER_NAME" \
  --restart unless-stopped \
  -p "127.0.0.1:5105:5105" \
  --env-file "$BASE/springboot.env" \
  "$IMAGE"

echo "→ docker image prune"
docker image prune -f

echo "→ docker ps"
docker ps --filter name="$CONTAINER_NAME"
# 中略：探针设计缘由的注释块（v0.1.x HEALTHCHECK 语义教训与 8080→5105 端口迁移说明），见源文件
i=0
while [ $i -lt 120 ]; do
  if wget --tries=1 --timeout=3 -q "http://127.0.0.1:5105/actuator/health" -O /dev/null 2>/dev/null; then
    echo "→ /actuator/health 200 (host 127.0.0.1:5105) after ${i}s"
    break
  fi
  # 容器实际死亡 (OOM / start-cmd failure / 立刻 crash) 提前终止循环, 立刻报失败。
  if ! docker inspect --format='{{.State.Running}}' "$CONTAINER_NAME" 2>/dev/null | grep -q true; then
    echo "→ container not running, logs:"
    docker logs --tail 30 "$CONTAINER_NAME"
    exit 1
  fi
  i=$((i+1))
  sleep 1
done

if [ $i -ge 120 ]; then
  echo "→ /actuator/health 仍未 200（120s 上限）, logs:"
  docker logs --tail 30 "$CONTAINER_NAME"
  exit 1
fi