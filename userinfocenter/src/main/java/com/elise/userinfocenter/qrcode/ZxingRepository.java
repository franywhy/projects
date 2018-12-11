package com.elise.userinfocenter.qrcode;


import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.zxing")
public class ZxingRepository {

    private Integer width;
    private Integer height;
    private String  characterSet;
    private Integer margin;
    private String  errorConnection;
    private Integer maxSize;
    private Integer minSize;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public ErrorCorrectionLevel getErrorConnection() {
        switch(errorConnection){
            case "L":
                return ErrorCorrectionLevel.L;
            case "M":
                return ErrorCorrectionLevel.M;
            case "Q":
                return ErrorCorrectionLevel.Q;
            case "H":
                return ErrorCorrectionLevel.H;
            default:
                return ErrorCorrectionLevel.L;
        }
    }

    public void setErrorConnection(String errorConnection) {
        this.errorConnection = errorConnection;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }
}
