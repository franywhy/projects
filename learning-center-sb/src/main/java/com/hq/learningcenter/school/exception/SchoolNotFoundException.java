package com.hq.learningcenter.school.exception;

/**
 * 找不到网校时抛出此异常。
 * @author XingNing OU
 */
public class SchoolNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2440449296467213137L;

    public SchoolNotFoundException() {
        super();
    }

    public SchoolNotFoundException(String message) {
        super(message);
    }

    public SchoolNotFoundException(Throwable cause) {
        super(cause);
    }

    public SchoolNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
