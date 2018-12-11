package com.hq.learningapi.entity;

/**
 * Created by Glenn on 2017/5/2 0002.
 */
public class AppGuideEntity {

    private Long guideId;
    private String guideName;
    private String guideUrl;
    private String guidePic;

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getGuideUrl() {
        return guideUrl;
    }

    public void setGuideUrl(String guideUrl) {
        this.guideUrl = guideUrl;
    }

    public String getGuidePic() {
        return guidePic;
    }

    public void setGuidePic(String guidePic) {
        this.guidePic = guidePic;
    }
}
