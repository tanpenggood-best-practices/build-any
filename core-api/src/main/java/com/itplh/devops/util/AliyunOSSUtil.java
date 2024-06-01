package com.itplh.devops.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class AliyunOSSUtil {

    public static void uploadFileToOSS(String bucketName,
                                       String objectName,
                                       String localFileAbsolutePath,
                                       CannedAccessControlList accessControlList) {
        Properties properties = new Properties();
        try {
            properties.load(FileUtil.getInputStreamReader("aliyun.properties"));
        } catch (IOException e) {
            // ignore
        }
        String accessKeyId = properties.getProperty("accessKeyId");
        String secretAccessKey = properties.getProperty("secretAccessKey");
        String ossEndpoint = properties.getProperty("ossEndpoint");

        log.info("upload file to oss, start.");
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, secretAccessKey);
        // 填写Bucket名称，例如examplebucket。
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        File file = new File(localFileAbsolutePath);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, credentialsProvider);

        try {
            log.info("object-name={} file-name={}", objectName, file.getName());
            ossClient.putObject(bucketName, objectName, FileUtil.getInputStream(file));
            ossClient.setObjectAcl(bucketName, objectName, accessControlList);
            log.info("upload file to oss, done.");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
