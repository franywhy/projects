package io.renren.pojo;

import java.io.Serializable;

/**
 * Created by DL on 2017/12/30.
 */
public class ColdStartingPOJO implements Serializable {
    //标题
    private String title;
    //图片
    private String pic;
    //地址
    private String url;
    //倒数秒数
    private String countdown;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    @Override
    public String toString() {
        return "ColdStartingPOJO{" +
                "title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", url='" + url + '\'' +
                ", countdown='" + countdown + '\'' +
                '}';
    }


}
