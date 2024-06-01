package com.itplh.demo;

import com.itplh.common.AppInfo;
import com.itplh.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = {"com.itplh"})
public class App1 {

    public static void main(String[] args) {
        SpringApplication.run(App1.class, args);
    }

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/app-info")
    public Result<AppInfo> appName() {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(appName);
        return Result.ok(appInfo);
    }

}
