package com.itplh.devops.service.impl;

import com.itplh.devops.domain.AppMateInfo;
import com.itplh.devops.service.AbstractNpmDevOpsPipeline;

public class ViteProjectDevOpsPipeline extends AbstractNpmDevOpsPipeline {

    @Override
    public AppMateInfo appInfo() {
        return new AppMateInfo("vite-project", "vite-project", "");
    }

}
