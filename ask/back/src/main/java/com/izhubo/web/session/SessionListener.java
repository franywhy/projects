package com.izhubo.web.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	public static Map userMap = new HashMap();
	private SessionContext myc = SessionContext.getInstance();

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
//		System.out.println("start...");
		myc.AddSession(httpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
//		System.out.println("end...");
		HttpSession session = httpSessionEvent.getSession();
		myc.DelSession(session);
	}
}