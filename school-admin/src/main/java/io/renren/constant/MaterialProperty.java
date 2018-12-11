package io.renren.constant;

/**
 * 
 * @author Yuanjp
 * 资料属性
 *
 */
public enum MaterialProperty {
	BEFORECLASS(0), //课前
	MIDDLECLASS(1), //课中 
	AFTERCLASS(2); //课后

	private int value;

	private MaterialProperty(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	/**
     * 判断值是否属于枚举类的值
     * @param key
	 * @return 
     */
    public static  MaterialProperty isMaterialProperty(String key){
        for (MaterialProperty e: MaterialProperty.values()){
           if(key.equals(e.toString())){
        	   return e;
           }
        }
        return null;
    }
    
    /**
     * 通过value 枚举类的值
     * @param key
     * @return
     */
    public static  MaterialProperty getMaterialProperty(int  value){
        for (MaterialProperty e: MaterialProperty.values()){
           if(e.value==value){
        	   return e;
           }
        }
        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(MaterialProperty.getMaterialProperty(2));
	}
    
}
