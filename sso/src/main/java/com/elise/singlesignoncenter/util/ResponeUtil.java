package com.elise.singlesignoncenter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.prototype.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: shihongjie
 * @email: shihongjie@hengqijy.com
 * @date: 2018/1/31 17:52
 */
public class ResponeUtil {
    private static final Logger TRACER = LoggerFactory.getLogger(ResponeUtil.class);

    /**
     * respone write
     * @param httpServletResponse
     * @param transactionStatus
     */
    public static final void responseWriter(HttpServletResponse httpServletResponse , TransactionStatus transactionStatus){
        responseWriter(httpServletResponse , transactionStatus.value() , transactionStatus.getReasonPhrase());
    }
    /**
     * respone write
     * @param httpServletResponse
     * @param code
     * @param message
     */
    public static final void responseWriter(HttpServletResponse httpServletResponse , int code , String message){
        ObjectMapper mapper = new ObjectMapper();
        WrappedResponse response = WrappedResponse.generate(code, message, null);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.append(mapper.writeValueAsString(response));
            out.flush();
        } catch (IOException e) {
            TRACER.error("写入流失败", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
