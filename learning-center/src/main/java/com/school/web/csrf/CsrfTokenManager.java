package com.school.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A component that can manage the creation and validation (and implicit revocation) of CSRF tokens that may be used to prevent CSRF attacks
 * - for example, during HTML form POSTs.
 * @author XnOU
 */
public interface CsrfTokenManager {

    /**
     * The name of CSRF token. This name is used to obtain the token from requests and forms for example.
     * @return the name of the CSRF that will be used in form field for example.
     */
    String getTokenName();

    /**
     * Returns a new 1-time-use CSRF token. For an HTML form, every time the form is rendered, a <em>new</em> CSRF token should be embedded
     * in the form as a hidden field.
     * @param request the incoming request.
     * @param response the outbound response.
     * @return a new 1-time-use CSRF token.
     */
    String createCsrfToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * Get the CSRF token from HTTP request.
     * @param request the inbound request.
     * @param response the outbound request.
     * @return the CSRF token or {@code null}.
     */
    String getTokenFromRequest(HttpServletRequest request, HttpServletResponse response);

    /**
     * Return {@code true} is the specified token is valid and the request (e.g. form post) should be allowed to be
     * processed, or {@code false} if the token is not valid and request processing should be disallowed. In either case,
     * the token is consumed and may not be used again (this method is not idempotent).
     * 
     * <h4>Forms</h4>
     * <p>
     * Every time a form is submitted, the token (presumably submitted in a hidden form field) must be checked for validity with this
     * method. If the token is valid, form processing is allowed. If the token is not valid,
     * the form post should be rejected. In either case, if showing the form again, a <b>new</b> token <em>must</em> be
     * {@link #createCsrfToken(HttpServletRequest, HttpServletResponse)} and embedded in the form as a hidden form field to ensure it is
     * submitted on the next form post.
     * </p>
     * @param request the inbound request.
     * @param response the outbound response.
     * @param csrfToken the CSRF token discovered during the request. Can be null.
     * @return {@code true} if the specified token is valid and the request (e.g. form post) should be allowed to be processed, or
     *         {@code false} if the token is not valid and request processing should be disallowed.
     */
    boolean isValidCsrfToken(HttpServletRequest request, HttpServletResponse response, String csrfToken);

}
