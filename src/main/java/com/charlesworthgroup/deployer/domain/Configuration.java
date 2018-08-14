package com.charlesworthgroup.deployer.domain;

public class Configuration {

    private String explodedName;
    private String warUrl;
    private String currentDirectory;
    private String startCmd;
    private String stopCmd;

    public String getExplodedName() {
        return explodedName;
    }

    public void setExplodedName(String explodedName) {
        this.explodedName = explodedName;
    }

    public String getWarUrl() {
        return warUrl;
    }

    public void setWarUrl(String warUrl) {
        this.warUrl = warUrl;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public String getStartCmd() {
        return startCmd;
    }

    public void setStartCmd(String startCmd) {
        this.startCmd = startCmd;
    }

    public String getStopCmd() {
        return stopCmd;
    }

    public void setStopCmd(String stopCmd) {
        this.stopCmd = stopCmd;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "explodedName='" + explodedName + '\'' +
                ", warUrl='" + warUrl + '\'' +
                ", currentDirectory='" + currentDirectory + '\'' +
                ", startCmd='" + startCmd + '\'' +
                ", stopCmd='" + stopCmd + '\'' +
                '}';
    }
}
