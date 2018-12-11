package com.hq.learningcenter.school.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class MyFreeMarkerView extends FreeMarkerView {

	private static final String CONTEXT_PATH = "base";
	private static final String SERVLET_PATH = "servletPath";
	
    @Override
    protected void exposeHelpers(Map<String, Object> model,
        HttpServletRequest request) throws Exception {
        model.put(CONTEXT_PATH, request.getContextPath());
        model.put(SERVLET_PATH, request.getServletPath());
        super.exposeHelpers(model, request);
    }
}
