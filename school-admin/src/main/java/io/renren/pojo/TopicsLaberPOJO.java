package io.renren.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/5/28.
 */

public class TopicsLaberPOJO implements Serializable {
    private String code;
    private List<Map<String,Object>> data;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
