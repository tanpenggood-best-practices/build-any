package com.itplh.devops.service.impl;

import com.itplh.devops.domain.AppMateInfo;
import com.itplh.devops.service.AbstractJavaDevOpsPipeline;

public class App2DevOpsPipeline extends AbstractJavaDevOpsPipeline {

    @Override
    public AppMateInfo appInfo() {
        return new AppMateInfo("app2", "app2", "app2.jar");
    }


}
