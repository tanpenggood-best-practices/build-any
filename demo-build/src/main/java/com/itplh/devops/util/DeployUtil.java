package com.itplh.devops.util;

import com.itplh.devops.service.DevOpsPipeline;

import java.io.IOException;

public class DeployUtil {

    public static void deployApp(DevOpsPipeline pipeline) throws IOException {
//        pipeline.gitCheckoutAndPull();
        pipeline.build();
//        pipeline.uploadToOSS();
//        pipeline.deploy();
//        pipeline.liveness();
    }

}
