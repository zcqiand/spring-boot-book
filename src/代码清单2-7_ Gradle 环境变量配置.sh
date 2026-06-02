# Windows PowerShell
$env:GRADLE_HOME = "D:\tools\gradle-8.10"
$env:PATH = "$env:GRADLE_HOME\bin;$env:PATH"

# macOS/Linux
export GRADLE_HOME=/usr/local/gradle-8.10
export PATH=$GRADLE_HOME/bin:$PATH