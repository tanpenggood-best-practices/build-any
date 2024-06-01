package com.itplh.integration.service.impl;

import com.itplh.common.AppInfo;
import com.itplh.integration.service.AppInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppInfoServiceImpl implements AppInfoService {

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Override
    public AppInfo getAppInfo() {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(appName);
        return appInfo;
    }

}
