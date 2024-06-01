package com.itplh.devops.service;

import com.itplh.devops.util.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.parsing.PropertyParser;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public abstract class AbstractNpmDevOpsPipeline extends AbstractDevOpsPipeline {

    @Override
    public void build() throws IOException {
        // npm install & npm run build
        String command = "cd ${parentPomDir} && ${npm} install && ${npm} run ${buildScript}";
        Properties buildProperties = getBuildProperties();
        command = PropertyParser.parse(command, buildProperties);
        CommandUtil.executeCommand(command);
    }

}
