package com.hq.learningcenter.pojo;

/**
 * Created by Glenn on 2017/4/25 0025.
 */
public class Token {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    @Override
    public String toString(){
          StringBuilder sb = new StringBuilder();
          sb.append("\n[Token]");
          sb.append("\nToken:");
          sb.append(token);
          return sb.toString();
    }
}
