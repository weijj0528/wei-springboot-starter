## wei-springboot-starter 脚手架

```shell

mvn archetype:generate \
    -DarchetypeGroupId=com.github.weijj0528 \
    -DarchetypeArtifactId=springboot-starter-archetype \
    -DarchetypeVersion=3.1.0-SNAPSHOT \
    -DgroupId=项目组名 \
    -DartifactId=项目名称 \
    -Dpackage=包名 

# 示例
mvn archetype:generate  -DarchetypeGroupId=com.github.weijj0528  -DarchetypeArtifactId=springboot-starter-archetype  -DarchetypeVersion=3.1.0-SNAPSHOT  -DgroupId=com.weijj0528.mall  -DartifactId=wei-mall  -Dpackage=com.weijj0528.mall

```