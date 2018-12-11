package com.hq.learningcenter.school.web.csrf;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link CsrfTokenManager} interface. To ensure correct behavior, the specified TTL <em>MUST</em> be the
 * equal to or greater than the specified nonce cache's TTL.
 * @author XnOU
 */
@Service("csrfTokenManager")
public class DefaultCsrfTokenManager implements CsrfTokenManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCsrfTokenManager.class);

    private static final String DEFAULT_CSRF_TOKEN_NAME = "csrfToken";

    private String tokenName;

    public DefaultCsrfTokenManager() {
        this(null);
    }

    public DefaultCsrfTokenManager(String tokenName) {
        if (null == tokenName || tokenName.isEmpty()) {
            this.tokenName = DEFAULT_CSRF_TOKEN_NAME;
        } else {
            this.tokenName = tokenName;
        }

    }

    public void setTokenName(String tokenName) {
        if (null == tokenName || tokenName.isEmpty()) {
            this.tokenName = DEFAULT_CSRF_TOKEN_NAME;
        } else {
            this.tokenName = tokenName;
        }
    }

    @Override
    public String getTokenName() {
        return this.tokenName;
    }

    @Override
    public String createCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        String csrfToken = UUID.randomUUID().toString().replace("-", "");

        request.getSession().setAttribute(getTokenName(), csrfToken);

        return csrfToken;
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = request.getParameter(getTokenName());
        if (null == tokenValue || tokenValue.isEmpty()) {
            tokenValue = request.getHeader(getTokenName());
        }
        return tokenValue;
    }

    @Override
    public boolean isValidCsrfToken(HttpServletRequest request, HttpServletResponse response, String csrfToken) {

        if (null == csrfToken) {
            return false;
        }

        Object sessionCsrfToken = request.getSession().getAttribute(getTokenName());

        if (null != sessionCsrfToken && sessionCsrfToken.equals(csrfToken)) {
            return true;
        } else {
            LOG.warn("CSRF token is null or sessionCsrfToken={}, requestCsrfToken={}", sessionCsrfToken, csrfToken);
            return false;
        }
    }

}
