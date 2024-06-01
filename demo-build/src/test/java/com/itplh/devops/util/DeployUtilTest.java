package com.itplh.devops.util;

import com.itplh.devops.service.impl.App1DevOpsPipeline;
import com.itplh.devops.service.impl.ViteProjectDevOpsPipeline;
import com.itplh.devops.service.impl.App2DevOpsPipeline;
import org.junit.Test;

import java.io.IOException;

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