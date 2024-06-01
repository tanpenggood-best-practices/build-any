package com.itplh.devops.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandUtil {

    public static void executeCommand(String command) throws IOException {
        execute(command, TypeEnum.COMMAND, null);
    }

    public static void executeCommand(String command, Charset charset) throws IOException {
        execute(command, TypeEnum.COMMAND, charset);
    }

    private static void execute(String command,
                                TypeEnum type,
                                Charset charset) throws IOException {
        if (command == null) {
            return;
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 将异常信息重定向到正常信息流
        processBuilder.redirectErrorStream(true);

        List<String> commands = new ArrayList<>();
        String os = System.getProperty("os.name");
        if (os == null) {
            return;
        }
        // Windows
        if (os.toLowerCase().contains("windows")) {
            // /c 执行完命令后关闭命令窗口
            // /k 执行完命令后不关闭命令窗口
            commands.addAll(Arrays.asList("cmd.exe", "/c"));
        }
        // Linux
        if (os.toLowerCase().contains("linux")
                || os.toLowerCase().contains("mac")) {
            if (Objects.equals(TypeEnum.COMMAND, type)) {
//                commands.addAll(Arrays.asList("/bin/bash", "-c"));
                commands.addAll(Arrays.asList("bash" , "-c"));
            }
            if (Objects.equals(TypeEnum.SCRIPT, type)) {
                commands.addAll(Arrays.asList("/bin/bash"));
            }
        }
        // 拼接命令
        commands.add(command);
        processBuilder.command(commands.toArray(new String[0]));
        System.out.println(processBuilder.command());
        // 启动进程
        Process process = processBuilder.start();
        if (charset == null) {
            // get os default charset
            Object osDefaultCharset = System.getProperties().get("sun.jnu.encoding");
            // 兜底策略
            charset = osDefaultCharset == null ? Charset.defaultCharset() : Charset.forName(osDefaultCharset.toString());
        }
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = process.waitFor();
            System.out.println("Exited with error code : " + exitVal);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    enum TypeEnum {
        COMMAND,
        SCRIPT
    }

}
