package com.itplh.devops.service;

import com.itplh.devops.domain.AppMateInfo;

import java.io.IOException;

public interface DevOpsPipeline {

    AppMateInfo appInfo();

    void gitCheckoutAndPull() throws IOException;

    void build() throws IOException;

    default void uploadToOSS() throws IOException {
    }

    default String deploy() throws IOException {
        return null;
    }

    default void liveness() throws IOException {
    }

}
