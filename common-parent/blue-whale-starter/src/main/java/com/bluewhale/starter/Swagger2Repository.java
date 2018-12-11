package com.bluewhale.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Glenn on 2017/8/7 0024.
 */

@ConfigurationProperties(prefix = "swagger")
public class Swagger2Repository {

    private Boolean enable = false;

    private String basePackage = "default";

    private String titleName = "default";

    private String author = "default";

    private String version = "1.0";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
