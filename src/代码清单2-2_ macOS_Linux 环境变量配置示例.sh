# 编辑 ~/.bash_profile 或 ~/.zshrc
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
source ~/.bash_profile  # 或 source ~/.zshrc
java -version