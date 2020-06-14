package me.selenium.framework.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("webdriver")
public class WebDriverSettings {
    private String type = "firefox";
    private String path;
    private String gridhubServer;
    private int elementTimeout = 30;
    private String position = "0,0";
    private String dimension = "800x600";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGridhubServer() {
        return gridhubServer;
    }

    public void setGridhubServer(String gridhubServer) {
        this.gridhubServer = gridhubServer;
    }

    public int getElementTimeout() {
        return elementTimeout;
    }

    public void setElementTimeout(int elementTimeout) {
        this.elementTimeout = elementTimeout;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
