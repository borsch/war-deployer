package com.charlesworthgroup.deployer;

import com.charlesworthgroup.deployer.domain.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = parseConfig();

        String cwd = configuration.getCurrentDirectory();
        String explodedFolder = cwd + "\\" + configuration.getExplodedName();
        String explodedWar = cwd + "\\" + configuration.getExplodedName() + ".war";

        File tempFile = new File("temp-file");

        try {
            Log.info("Downloading file from [%s]", configuration.getWarUrl());

            DownloadUtil.download(configuration.getWarUrl(), tempFile);
        } catch (Exception e) {
            Log.error("Can not download file. Reason: [%s]", e.getMessage());
            return;
        }

        try {
            Log.info("Executing stop cmd [%s]", configuration.getStopCmd());
            executeCommand(configuration.getStopCmd());
        } catch (InterruptedException | IOException e) {
            Log.error("Can not execute stop CMD");
            return;
        }

        try {
            Log.info("Create ZIP arcive with old war file");

            ZipUtil.createZip(cwd + "\\", explodedFolder, explodedWar);

            Utils.deleteFile(new File(explodedWar));
            Utils.deleteFile(new File(explodedFolder));

            tempFile.renameTo(new File(explodedWar));
        } catch (IOException e) {
            Log.error("Can not create zip file");
        }

        try {
            Log.info("Executing start cmd [%s]", configuration.getStartCmd());

            executeCommand(configuration.getStartCmd());
        } catch (InterruptedException | IOException e) {
            Log.error("Can not execute start CMD. Please do it manually");
        }
    }

    private static Configuration parseConfig() {
        String currentDirectory = FileSystems.getDefault().getPath("").toFile().getAbsolutePath();

        Log.info("Current worked directory - [%s]", currentDirectory);

        try {
            Configuration configuration = new Configuration();

            configuration.setCurrentDirectory(currentDirectory);

            String configText = Utils.readFile(currentDirectory + "\\" + CONFIGURATION_FILE);
            String[] lines = configText.split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("=");

                String paramName = parts[0].trim();
                String paramValue = parts[1].trim();

                if (paramName.equals("old-war-name")) {
                    configuration.setExplodedName(paramValue);
                } else if (paramName.equals("new-war-url")) {
                    configuration.setWarUrl(paramValue);
                } else if (paramName.equals("start-cmd")) {
                    configuration.setStartCmd(paramValue);
                } else if (paramName.equals("stop-cmd")) {
                    configuration.setStopCmd(paramValue);
                }
            }

            return configuration;
        } catch (IOException e) {
            Log.error("Can not read configuration file. check is current directory contains file [%s]", CONFIGURATION_FILE);

            System.exit(1);

            return null;
        }
    }

    private static void executeCommand(String cmd) throws IOException, InterruptedException {
        if (cmd == null || cmd.isEmpty())
            return;

        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
    }

    private static final String CONFIGURATION_FILE = "config.cfg";

}
