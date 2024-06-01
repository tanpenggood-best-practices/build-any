package com.itplh.devops.service;

import com.itplh.devops.util.CommandUtil;
import com.itplh.devops.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.parsing.PropertyParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public abstract class AbstractDevOpsPipeline implements DevOpsPipeline {

    private Properties aliyunProperties = new Properties();
    private Properties appProperties = new Properties();
    private Properties buildProperties = new Properties();
    private Properties edasFormProperties = new Properties();

    {
        try {
            Optional.ofNullable(FileUtil.getInputStreamReader(getAliyunPropertiesClasspath())).ifPresent(reader -> load(aliyunProperties, reader));
            Optional.ofNullable(FileUtil.getInputStreamReader(getAppPropertiesClasspath())).ifPresent(reader -> load(appProperties, reader));
            Optional.ofNullable(FileUtil.getInputStreamReader(getBuildPropertiesClasspath())).ifPresent(reader -> load(buildProperties, reader));
            Optional.ofNullable(FileUtil.getInputStreamReader(getEDASFormPropertiesClasspath())).ifPresent(reader -> load(edasFormProperties, reader));
        } catch (Throwable e) {
            log.error("Has an error = {}", e.getMessage(), e);
        }
    }

    @Override
    public void gitCheckoutAndPull() throws IOException {
        // git checkout ${targetBranch}
        // git pull --progress -v --no-rebase "origin"
        String command = "cd ${projectRootDir} & ${git} checkout \"${targetBranch}\" & ${git} pull --progress -v --no-rebase \"origin\"";
        Properties buildProperties = getBuildProperties();
        command = PropertyParser.parse(command, buildProperties);
        CommandUtil.executeCommand(command, StandardCharsets.UTF_8);
    }

    protected Properties getAliyunProperties() {
        return aliyunProperties;
    }

    protected Properties getAppProperties() {
        return appProperties;
    }

    protected Properties getBuildProperties() {
        return buildProperties;
    }

    protected Properties getEDASFormProperties() {
        return edasFormProperties;
    }

    private String getAliyunPropertiesClasspath() {
        return "aliyun.properties";
    }

    private String getAppPropertiesClasspath() {
        return String.format("devops/%s/app.properties", this.appInfo().getAppAlias());
    }

    private String getBuildPropertiesClasspath() {
        return String.format("devops/%s/build.properties", this.appInfo().getAppAlias());
    }

    private String getEDASFormPropertiesClasspath() {
        return String.format("devops/%s/edas-publish-form-data.properties", this.appInfo().getAppAlias());
    }

    private void load(Properties properties , Reader reader) {
        try {
            properties.load(reader);
        } catch (IOException e) {
            // ignore
        }
    }

}
