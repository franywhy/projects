package com.bluewhale.common.interfaze;

import com.bluewhale.common.enumeration.TransactionStatus;
import com.bluewhale.common.prototype.WrappedResponse;
import com.bluewhale.common.util.SchoolIdUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 提供Restful服务的抽象基类。
 *
 * @author Glenn
 */
public abstract class AbstractRestController<T> {

    protected Logger TRACER = LoggerFactory.getLogger(this.getClass());

    protected String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if ((null != ip) && (ip.length() > 0)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    protected String getRequestMethod(HttpServletRequest request) {
        return request.getMethod();

    }

    protected String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    protected String getSchoolId(HttpServletRequest request) {
        return SchoolIdUtil.getSchoolId(request);
    }

    /**
     * 处理成功的HTTP响应内容，响应200状态码。
     */
    protected ResponseEntity<WrappedResponse<T>> success() {
        return this.success(null);
    }

    /**
     * 处理成功的HTTP响应内容，响应200状态码。
     */
    protected ResponseEntity<WrappedResponse<T>> success(Object data) {

        return this.success(data, TransactionStatus.OK);
    }

    /**
     * 处理成功的HTTP响应内容，响应200状态码。
     */
    protected ResponseEntity<WrappedResponse<T>> success(Object data, Cookie cookie) {
        return this.success(data, TransactionStatus.OK, cookie);

    }

    /**
     * 处理成功的HTTP响应内容,返回2XX结果
     *
     * @param data 响应结果
     */
    protected ResponseEntity<WrappedResponse<T>> success(Object data, TransactionStatus status, Cookie cookie) {
        if (status.is2xxSuccessful()) {
            return entityGenerator(data, status.getReasonPhrase(), status, cookie);
        } else {
            return entityGenerator(data, TransactionStatus.OK.getReasonPhrase(), TransactionStatus.OK, cookie);
        }
    }

    protected ResponseEntity<WrappedResponse<T>> success(Object data, TransactionStatus status) {
        if (status.is2xxSuccessful()) {
            return entityGenerator(data, status.getReasonPhrase(), status, null);
        } else {
            return entityGenerator(data, TransactionStatus.OK.getReasonPhrase(), TransactionStatus.OK, null);
        }
    }

    /**
     * 处理失败，抛出异常，响应500状态码。
     *
     * @param message 失败代码或提示
     */
    protected ResponseEntity<WrappedResponse<T>> fail(String message) {
        return this.fail(message, TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理失败，抛出异常，响应5XX状态码。
     *
     * @param status Transaction状态码
     */
    protected ResponseEntity<WrappedResponse<T>> fail(TransactionStatus status) {
        return this.fail(status.getReasonPhrase(), status);
    }

    /**
     * 处理失败，抛出异常,输出指定message代替Transaction默认提示语。
     * 响应5XX状态码。
     *
     * @param message 失败代码或提示
     * @param status  Transaction状态码
     */
    protected ResponseEntity<WrappedResponse<T>> fail(String message, TransactionStatus status) {
        if (status.is5xxServerError()) {
            return entityGenerator(null, message, status, null);
        } else {
            return entityGenerator(null, message, TransactionStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * 发生错误，由用户提交的参数非法而引起，响应400状态码。
     *
     * @param message 错误代码或提示
     */
    protected ResponseEntity<WrappedResponse<T>> error(String message) {
        return this.error(message, TransactionStatus.BAD_REQUEST);
    }

    /**
     * 发生错误，由用户提交的参数非法而引起，响应4XX状态码。
     *
     * @param status Transaction状态码
     */
    protected ResponseEntity<WrappedResponse<T>> error(TransactionStatus status) {
        return this.error(status.getReasonPhrase(), status);
    }

    /**
     * 发生错误，由用户提交的参数非法而引起，响应4XX状态码。
     *
     * @param message 错误提示
     * @param status  Transaction状态码
     */
    protected ResponseEntity<WrappedResponse<T>> error(String message, TransactionStatus status) {
        if (status.is4xxClientError()) {
            return entityGenerator(null, message, status, null);
        } else {
            return entityGenerator(null, message, TransactionStatus.BAD_REQUEST, null);
        }
    }

    protected ResponseEntity<WrappedResponse<T>> entityGenerator(Object data, String message, TransactionStatus status, Cookie cookie) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (cookie != null) {
            if (cookie.getName() != null && cookie.getValue() != null) {
                StringBuilder cookieBuilder = new StringBuilder();
                // set cookie key and value
                cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue());
                cookieBuilder.append(";");
                // set cookie domain
                if (cookie.getDomain() != null) {
                    cookieBuilder.append("Domain=").append(cookie.getDomain()).append(";");
                }
                //set cookie path
                if (cookie.getPath() != null) {
                    cookieBuilder.append("Path=").append(cookie.getPath()).append(";");
                } else {
                    cookieBuilder.append("Path=").append("/").append(";");
                }
                //set cookie expire time
                if (cookie.getMaxAge() > 0) {
                    cookieBuilder.append("Max-Age=").append(cookie.getMaxAge()).append(";");
                } else {
                    cookieBuilder.append("Max-Age=").append(60 * 60 * 24).append(";");
                }
                // if it is https , return
                if (cookie.getSecure()) {
                    cookieBuilder.append("SECURE;");
                }
                cookieBuilder.append("HttpOnly;");
                headers.set(HttpHeaders.SET_COOKIE, cookieBuilder.toString());
            }
        }
        WrappedResponse resp = WrappedResponse.generate(status.value(), message, data);
        ResponseEntity<WrappedResponse<T>> responseEntity = new ResponseEntity<>(resp, headers, HttpStatus.OK);
        return responseEntity;
    }

}