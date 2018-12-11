package io.renren.enums;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/19 16:15
 */
public enum AuditStatusEnum {
    daishenhe(0,"待审核"),quxiao(1,"作废"),tongguo(2,"通过");

    private int value;

    private String text;

    AuditStatusEnum(int type, String name) {
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
        for (AuditStatusEnum c : AuditStatusEnum.values()) {
            if (c.getValue() == index) {
                return c.text;
            }
        }
        return null;
    }
}
