package com.hq.answerapi.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Glenn on  2017/10/21 0021 10:24.
 */

@ConfigurationProperties(prefix = "kaptcha")
@Component
public class CaptchaProperties {

    private String border;
    private String borderColor;
    private String textProducerFondColor;
    private String imageWidth;
    private String imageHeight;
    private String sessionKey;
    private String textProducerCharLength;
    private String textProducerFontNames;
    private  boolean validate;

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getTextProducerFondColor() {
        return textProducerFondColor;
    }

    public void setTextProducerFondColor(String textProducerFondColor) {
        this.textProducerFondColor = textProducerFondColor;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getTextProducerCharLength() {
        return textProducerCharLength;
    }

    public void setTextProducerCharLength(String textProducerCharLength) {
        this.textProducerCharLength = textProducerCharLength;
    }

    public String getTextProducerFontNames() {
        return textProducerFontNames;
    }

    public void setTextProducerFontNames(String textProducerFontNames) {
        this.textProducerFontNames = textProducerFontNames;
    }
}
