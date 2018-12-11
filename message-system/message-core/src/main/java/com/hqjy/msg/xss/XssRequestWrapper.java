package com.hqjy.msg.xss;

import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/23 0023.
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static Policy policy = null;

    private static InputStream input = null;

    private static final AntiSamy antiSamy = new AntiSamy();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
        if (input == null) {
            try {
                input = this.getClass().getClassLoader().getResourceAsStream("antisamy-ebay.xml");
                policy = Policy.getInstance(input);
            } catch (PolicyException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> request_map = super.getParameterMap();
        Iterator iterator = request_map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me = (Map.Entry) iterator.next();
            String[] values = (String[]) me.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssClean(values[i]);
            }
        }
        return request_map;
    }

    public String[] getParameterValues(String paramString) {
        String[] arrayOfString1 = super.getParameterValues(paramString);
        if (arrayOfString1 == null)
            return null;
        int i = arrayOfString1.length;
        String[] arrayOfString2 = new String[i];
        for (int j = 0; j < i; j++) {
            arrayOfString2[j] = xssClean(arrayOfString1[j]);
        }
        return arrayOfString2;
    }

    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null)
            return null;
        if (paramString.equals("nickName")) {
            return str;
        }
        return xssClean(str);
    }

    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null)
            return null;
        return xssClean(str);
    }

    private String xssClean(String value) {
        try {
            if (value != null) {
                final CleanResults cr = antiSamy.scan(value, policy);
                //安全的HTML输出
                return cr.getCleanHTML();
            }
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
    }
}