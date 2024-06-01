package com.itplh.devops.service;

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.edas.model.v20170801.DeployK8sApplicationRequest;
import com.aliyuncs.edas.model.v20170801.DeployK8sApplicationResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.itplh.devops.util.AliyunOSSUtil;
import com.itplh.devops.util.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.parsing.PropertyParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Slf4j
public abstract class AbstractJavaDevOpsPipeline extends AbstractDevOpsPipeline {

    {
        // Dynamic load classworlds classpath by maven version
        Properties buildProperties = getBuildProperties();
        String mavenHome = buildProperties.getProperty("mavenHome");
        try {
            Files.list(Paths.get(mavenHome, "boot"))
                    .map(Path::toFile)
                    .filter(f -> f.getName().startsWith("plexus-classworlds-"))
                    .filter(f -> f.getName().endsWith(".jar"))
                    .filter(f -> f.isFile())
                    .findFirst()
                    .ifPresent(f -> buildProperties.put("classworldsClasspath", f.getAbsolutePath()));
        } catch (IOException e) {
            log.error("Has an error = {}", e.getMessage(), e);
        }
    }

    @Override
    public void build() throws IOException {
        mavenClean();
        mavenPackage();
    }

    @Override
    public void uploadToOSS() throws IOException {
        Properties buildProperties = getBuildProperties();
        String jar = buildProperties.getProperty("parentPomDir") + File.separator + buildProperties.getProperty("targetJar");
        String bucketName = buildProperties.getProperty("bucketName");

        AliyunOSSUtil.uploadFileToOSS(bucketName, "jar/" + appInfo().getBuildFinalName(),
                jar, CannedAccessControlList.PublicReadWrite);
    }

    @Override
    public String deploy() throws IOException {
        Properties aliyunProperties = getAliyunProperties();
        String accessKeyId = aliyunProperties.getProperty("accessKeyId");
        String secretAccessKey = aliyunProperties.getProperty("secretAccessKey");
        String regionId = aliyunProperties.getProperty("regionId");
        String edasDomain = aliyunProperties.getProperty("edasDomain");

        Properties edasFormProperties = getEDASFormProperties();
        edasFormProperties.setProperty("packageVersion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));

        String productName = "Edas";
        // 构建 OpenApi 客户端
        try {
            DefaultProfile.addEndpoint(regionId, regionId, productName, edasDomain);
        } catch (ClientException e) {
            // ignore
        }
        DefaultProfile defaultProfile = DefaultProfile.getProfile(regionId, accessKeyId, secretAccessKey);
        DefaultAcsClient defaultAcsClient = new DefaultAcsClient(defaultProfile);
        // 构建应用部署分组列表接口请求具体入参参考接口说明
        DeployK8sApplicationRequest request = new DeployK8sApplicationRequest();
        request.setAppId(edasFormProperties.getProperty("appId"));
        request.setJavaStartUpConfig(edasFormProperties.getProperty("javaStartUpConfig"));
        request.setUpdateStrategy(edasFormProperties.getProperty("updateStrategy"));
        request.setStorageType(edasFormProperties.getProperty("storageType"));
        request.setMemoryRequest(1024);
        request.setPackageUrl(edasFormProperties.getProperty("warUrl"));
        request.setPackageVersion(edasFormProperties.getProperty("packageVersion"));
        DeployK8sApplicationResponse acsResponse = null;
        try {
            acsResponse = defaultAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        }
        String responseBody = JSON.toJSONString(acsResponse);
        log.info("responseBody={}", responseBody);
        return responseBody;
    }

    private void mavenClean() throws IOException {
        // mvn -DskipTests=true clean -pl ${targetModule} -am
        String command = "cd \"${projectRootDir}\" && ${java} ${buildVMOptions} -Dmaven.multiModuleProjectDirectory=\"${parentPomDir}\" -Djansi.passthrough=true -Dmaven.home=\"${mavenHome}\" -Dclassworlds.conf=\"${mavenHome}/bin/m2.conf\" -Dfile.encoding=UTF-8 -classpath \"${classworldsClasspath}\" org.codehaus.classworlds.Launcher -s \"${settings}\" -DskipTests=true clean -pl \"${targetModule}\" -am";
        Properties buildProperties = getBuildProperties();
        command = PropertyParser.parse(command, buildProperties);
        CommandUtil.executeCommand(command);
    }

    private void mavenPackage() throws IOException {
        // mvn -DskipTests=true package -pl ${targetModule} -am
        String command = "cd \"${projectRootDir}\" && ${java} ${buildVMOptions} -Dmaven.multiModuleProjectDirectory=\"${parentPomDir}\" -Djansi.passthrough=true -Dmaven.home=\"${mavenHome}\" -Dclassworlds.conf=\"${mavenHome}/bin/m2.conf\" -Dfile.encoding=UTF-8 -classpath \"${classworldsClasspath}\" org.codehaus.classworlds.Launcher -s \"${settings}\" -DskipTests=true package -pl \"${targetModule}\" -am";
        Properties buildProperties = getBuildProperties();
        command = PropertyParser.parse(command, buildProperties);
        CommandUtil.executeCommand(command);
    }

}
