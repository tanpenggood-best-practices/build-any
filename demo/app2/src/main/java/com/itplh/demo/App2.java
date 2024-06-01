package com.itplh.demo;

import com.itplh.common.AppInfo;
import com.itplh.common.Result;
import com.itplh.integration.service.AppInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = {"com.itplh"})
public class App2 {

    public static void main(String[] args) {
        SpringApplication.run(App2.class, args);
    }

    private final AppInfoService appInfoService;

    @GetMapping("/app-info")
    public Result<AppInfo> appInfo() {
        return Result.ok(appInfoService.getAppInfo());
    }

}
