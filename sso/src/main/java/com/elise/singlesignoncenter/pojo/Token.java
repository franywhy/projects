package com.elise.singlesignoncenter.pojo;

/**
 * Created by Glenn on 2017/4/25 0025.
 */

public class Token {

    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
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
