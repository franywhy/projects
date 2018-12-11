package com.school.utils;

public enum LogEnum {
	DEFAULT("默认", 0),
	LIVE("直播", 1),
	REPLAY("回放", 2);

    private String name;  
    private int index;  
    
    private LogEnum(String name, Integer index) {  
        this.name = name;  
        this.index = index;  
    }  
    
    @Override  
    public String toString() {  
        return this.name;  
    } 
    
    public Integer getIndex() {
    	return this.index;
    }
}
