# 中略：log 提示行，见源文件
if ! id deploy >/dev/null 2>&1; then
  adduser --disabled-password --gecos "" --shell /bin/bash deploy
fi
usermod -aG docker deploy
# 中略：cert 目录块，两段非连续，见源文件

sudo -u deploy mkdir -p "$BASE"