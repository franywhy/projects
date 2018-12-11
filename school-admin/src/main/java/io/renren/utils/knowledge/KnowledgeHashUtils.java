package io.renren.utils.knowledge;

import io.renren.utils.ProduceToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KnowledgeHashUtils {

    private static final String KNOWLEDGE_MAIN_ID_SPLACE = "A";
    private static final String KNOWLEDGE_CHILD_ID_SPLACE = "B";
    private static final String DEFAULT_STR = "0";
    private static final Long DEFAULT_NO = 0L;

    public static String defaultStr(){
        return DEFAULT_STR;
    }
    public static Long defaultNo(){
        return DEFAULT_NO;
    }

    public static String toStr(List<KnowledgeHashObj> knowledgeHashObjs){
        String str = defaultStr();
        String append = append(knowledgeHashObjs);
        if(StringUtils.isNotBlank(append)){
            str= ProduceToken.getMD5(append);
        }
        return str;
    }

    /**
     *
     * @param knowledgeHashObjs
     * @return
     */
    private static String append(List<KnowledgeHashObj> knowledgeHashObjs){
        String str = null;
        if(CollectionUtils.isNotEmpty(knowledgeHashObjs)){
            compareKnowledgeHashObj(knowledgeHashObjs);
            StringBuffer sbf = new StringBuffer();
            for (int i = 0 , len = knowledgeHashObjs.size(); i < len ; i++ ){
                KnowledgeHashObj knowledgeHashObj = knowledgeHashObjs.get(i);
                sbf.append(knowledgeHashObj.getKnowledegId() + KNOWLEDGE_MAIN_ID_SPLACE);
                if(CollectionUtils.isNotEmpty(knowledgeHashObj.getChildList())){
                    List<Long> childList = knowledgeHashObj.getChildList();
                    compareLong(childList);
                    for (int j = 0 ; j < childList.size(); j++) {
                        Long childId = childList.get(j);
                        sbf.append(childId + KNOWLEDGE_CHILD_ID_SPLACE);
                    }
                }
            }
            str = sbf.toString();
        }
        return str;
    }

    /**
     * 排序List(Long)
     * @param longList
     */
    private static void compareLong(List<Long> longList){
        Collections.sort(longList, new Comparator<Long>() {
            @Override
            public int compare(final Long o1,final Long o2) {
                return (int) (o1 - o2);
            }
        });
    }

    /**
     * 排序List<KnowledgeHashObj>
     * @param knowledgeHashObjs
     */
    private static void compareKnowledgeHashObj(List<KnowledgeHashObj> knowledgeHashObjs){
        Collections.sort(knowledgeHashObjs, new Comparator<KnowledgeHashObj>() {
            @Override
            public int compare(KnowledgeHashObj o1, KnowledgeHashObj o2) {
                return (int) (o1.getKnowledegId() - o2.getKnowledegId());
            }
        });
    }
}
