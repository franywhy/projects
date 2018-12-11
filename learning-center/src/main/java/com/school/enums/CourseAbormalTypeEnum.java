package com.school.enums;

/**
 * @author linchaokai
 * @Description 异常单枚举
 */
public enum CourseAbormalTypeEnum {
    //异常单类型
    qita(-1,"其他"),normal(0,"正常"),xiuxue(1,"休学"),shilian(2,"失联"),qikao(3,"弃考"),miankao(4,"免考");

    private int value;

    private String text;

    CourseAbormalTypeEnum(int type, String name) {
        this.value = type;
        this.text = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取枚举名称
     * @param index 值
     * @return
     */
    public static String getText(int index) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getValue() == index) {
                return c.text;
            }
        }
        return null;
    }

    /**
     * 获取枚举值
     * @param text 枚举值
     * @return
     */
    public static Integer getValue(String text) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getText().equals(text)) {
                return c.value;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getE(1));
    }


    /**
     * 获取枚举对象
     * @param text 枚举名称
     * @return
     */
    public static CourseAbormalTypeEnum getE(String text) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getText().equals(text)) {
                return c;
            }
        }
        return null;
    }

    /**
     * 获取枚举对象
     * @param value 枚举值
     * @return
     */
    public static CourseAbormalTypeEnum getE(Integer value) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getValue()==(value.intValue())) {
                return c;
            }
        }
        return null;
    }
}
