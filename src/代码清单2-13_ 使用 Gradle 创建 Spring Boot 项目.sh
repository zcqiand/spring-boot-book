curl https://start.spring.io/starter.zip \
  -d type=gradle-project \
  -d language=java \
  -d bootVersion=3.4.3 \
  -d baseDir=demo-gradle \
  -d groupId=com.example \
  -d artifactId=demo-gradle \
  -d name=demo-gradle \
  -d packageName=com.example.demo \
  -d javaVersion=17 \
  -d dependencies=web \
  -o demo-gradle.zip

unzip demo-gradle.zip
cd demo-gradle
cat build.gradle