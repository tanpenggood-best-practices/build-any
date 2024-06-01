# Build Any

> Build maven project, build frontend project, build any ...

```java
public interface DevOpsPipeline {

    AppMateInfo appInfo();

    void gitCheckoutAndPull() throws IOException;

    void build() throws IOException;

    default void uploadToOSS() throws IOException {
    }

    default String deploy() throws IOException {
        return null;
    }

    default void liveness() throws IOException {
    }

}
```

## Project structure

```
build-any
├─core-api                                # Build pipeline api
│  └─com.itplh.devops.service
│     ├─DevOpsPipeline.java
│     ├─AbstractDevOpsPipeline.java
│     ├─AbstractJavaDevOpsPipeline.java
│     └─AbstractNpmDevOpsPipeline.java
├─demo-build                              # Build pipeline implement
│  └─com.itplh.devops.service.impl
│     ├─App1DevOpsPipeline.java
│     ├─App2DevOpsPipeline.java
│     └─ViteProjectDevOpsPipeline.java
├─demo                                    # A maven multiple module project
│  ├─common
│  ├─intrgration
│  ├─app1                                 # backend project, app1
│  ├─app2                                 # backend project, app2
│  └─vite-project                         # frontend project
└─README.md
```
## Usage

```java
public class DeployUtilTest {

    @Test
    public void deployApp1() throws IOException {
        DeployUtil.deployApp(new App1DevOpsPipeline());
    }

    @Test
    public void deployApp2() throws IOException {
        DeployUtil.deployApp(new App2DevOpsPipeline());
    }

    @Test
    public void deployViteProject() throws IOException {
        DeployUtil.deployApp(new ViteProjectDevOpsPipeline());
    }

}
```

## Sample: build.properties

```properties
# project root dir of need build
projectRootDir=/Users/ios/tcode/deploy/build-any
parentPomDir=/Users/ios/tcode/deploy/build-any
# mvn clean package -pl ${targetModule} -am
targetModule=demo/app1
# jar name after maven package
targetJar=app1/target/app1.jar
# git command absolute path
git=/usr/bin/git
# build target git branch
targetBranch=main
# java command absolute path
java=/Users/ios/Library/Java/JavaVirtualMachines/temurin-1.8.0_402/Contents/Home/bin/java
# maven build vm options
buildVMOptions=-Xmx512m
# maven home
mavenHome=/Users/ios/Downloads/apache-maven-3.6.0
# maven settings absolute path
settings=/Users/ios/Downloads/settings.xml
# upload oss bucket name
bucketName=oss-app-dev
```