# 中略：各步骤 echo 提示行，见源文件
printf '%s' "$PASSWORD" | docker login -u "$USERNAME" --password-stdin
docker pull "$IMAGE"

docker stop "$CONTAINER_NAME" 2>/dev/null || true
docker rm "$CONTAINER_NAME" 2>/dev/null || true

docker run -d \
  --name "$CONTAINER_NAME" \
  --restart unless-stopped \
  -p "127.0.0.1:5205:5205" \
  --env-file "$BASE/springboot.env" \
  "$IMAGE"

docker image prune -f
# 中略：docker ps 回显，见源文件

i=0
while [ $i -lt 120 ]; do
  if wget --tries=1 --timeout=3 -q "http://127.0.0.1:5205/actuator/health" -O /dev/null 2>/dev/null; then
    echo "→ /actuator/health 200 (host 127.0.0.1:5205) after ${i}s"
    break
  fi
  if ! docker inspect --format='{{.State.Running}}' "$CONTAINER_NAME" 2>/dev/null | grep -q true; then
    echo "→ container not running, logs:"
    docker logs --tail 30 "$CONTAINER_NAME"
    exit 1
  fi
  i=$((i+1))
  sleep 1
done
# 中略：循环后 120 秒超时分支（打日志并退出），见源文件