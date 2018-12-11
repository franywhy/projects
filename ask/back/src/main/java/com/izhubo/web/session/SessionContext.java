package com.izhubo.web.session;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class SessionContext {  
    private static SessionContext instance;  
    private HashMap mymap;  
  
    private SessionContext() {  
        mymap = new HashMap();  
    }  
  
    public static SessionContext getInstance() {  
        if (instance == null) {  
            instance = new SessionContext();  
        }  
        return instance;  
    }  
  
    @SuppressWarnings("unchecked")  
    public synchronized void AddSession(HttpSession session) {  
        if (session != null) {  
            mymap.put(session.getId(), session);  
        }  
    }  
  
    public synchronized void DelSession(HttpSession session) {  
        if (session != null) {  
            mymap.remove(session.getId());  
        }  
    }  
  
    public synchronized HttpSession getSession(String session_id) {  
        if (session_id == null)  
            return null;  
        return (HttpSession) mymap.get(session_id);  
    }  
  
}  