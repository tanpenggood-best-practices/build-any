package com.itplh.devops.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppMateInfo {

    private String appName;
    private String appAlias;
    private String buildFinalName;

}
