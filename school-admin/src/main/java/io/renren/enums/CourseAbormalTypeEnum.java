package io.renren.enums;

public enum CourseAbormalTypeEnum {
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

    // 普通方法
    public static String getText(int index) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getValue() == index) {
                return c.text;
            }
        }
        return null;
    }
  // 普通方法
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


    // 普通方法
    public static CourseAbormalTypeEnum getE(String text) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getText().equals(text)) {
                return c;
            }
        }
        return null;
    }

    // 普通方法
    public static CourseAbormalTypeEnum getE(Integer value) {
        for (CourseAbormalTypeEnum c : CourseAbormalTypeEnum.values()) {
            if (c.getValue()==(value.intValue())) {
                return c;
            }
        }
        return null;
    }
}
