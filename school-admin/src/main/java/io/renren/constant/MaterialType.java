package io.renren.constant;

/**
 * 资料类型
 * 
 * @author Yuanjp
 *
 */
public enum MaterialType {
	VIDEO(0), // 视频
	LIVE(1), // 直播
	QUESTIONBANK(2), // 题库
	TRAINING(3), // 实训
	FILE(4), // 文件
	FACETOFACE(5), // 面授
	OTHER(6); // 其他

	private int value;

	private MaterialType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
     * 通过key 判断值是否属于枚举类的值
     * @param key
     * @return
     */
    public static  MaterialType isMaterialType(String key){
        for (MaterialType e: MaterialType.values()){
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
    public static  MaterialType getMaterialType(int  value){
        for (MaterialType e: MaterialType.values()){
           if(e.value==value){
        	   return e;
           }
        }
        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(MaterialType.getMaterialType(5));
	}
    
}
