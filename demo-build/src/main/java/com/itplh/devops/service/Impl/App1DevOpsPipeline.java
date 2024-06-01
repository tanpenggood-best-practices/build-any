package com.itplh.devops.service.Impl;

import com.itplh.devops.domain.AppMateInfo;
import com.itplh.devops.service.AbstractJavaDevOpsPipeline;

public class App1DevOpsPipeline extends AbstractJavaDevOpsPipeline {

    @Override
    public AppMateInfo appInfo() {
        return new AppMateInfo("app1", "app1", "app1.jar");
    }

}
