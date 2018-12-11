package com.elise.userinfocenter.pojo;

/**
 * Created by Glenn on 2017/5/6 0006.
 */
public class TokenStatus {

    private Boolean isExpired;
    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n[TokenStatus]");
        sb.append("\nisExpired:");
        sb.append(isExpired);
        return sb.toString();
    }
}
