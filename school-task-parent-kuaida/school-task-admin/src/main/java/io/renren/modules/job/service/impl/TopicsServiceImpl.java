package io.renren.modules.job.service.impl;

import com.mongodb.client.MongoCollection;
import io.renren.modules.job.service.CourseUserplanDetailService;
import io.renren.modules.job.service.TopicsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by longduyuan on 2018/11/22 0022.
 * @author hq
 */
@Service("topicsService")
public class TopicsServiceImpl implements TopicsService {

    @Autowired
    private MongoTemplate xyMongoTemplate;

    @Autowired
    private CourseUserplanDetailService courseUserplanDetailService;

    private static final int TEA_USER_ID = 0;
    private static final int STU_USER_ID = 1;
    private static final String PIC_STUDENT = "http://answerimg.kjcity.com/default_student.png";
    private static final String PIC_TEACHER = "http://answerimg.kjcity.com/default_teacher.png";

    private MongoCollection<Document> users(){
        return xyMongoTemplate.getCollection("users");
    }

    private MongoCollection<Document> topics(){
        return xyMongoTemplate.getCollection("topics");
    }

    private MongoCollection<Document> topics_reply(){
        return xyMongoTemplate.getCollection("topics_reply");
    }

    @Override
    public void save(Map<String,Object> topicsGensee, String classplanLiveId, String productName) {
        /**
         * 根据 (submitter + question + submitTime) md5 之后生成 展示互动 问题ID 判断是否属于同一个问题 去重
         * 根据 (answerBy + response + answerTime) md5 之后生成 展示互动 问题回复ID 判断是否属于同一个问题回复 去重
         */
        String topic_gensee_id = DigestUtils.md5Hex(new StringBuilder().append(topicsGensee.get("submitter"))
                .append(topicsGensee.get("question")).append(topicsGensee.get("submitTime")).toString());
        String topic_reply_gensee_id = DigestUtils.md5Hex(new StringBuilder().append(topicsGensee.get("answerBy"))
                .append(topicsGensee.get("response")).append(topicsGensee.get("answerTime")).toString());

        //七天前
        long seven_days_ago = System.currentTimeMillis() - 604800000;
        Document isExist = topics().find(and(gte("timestamp",seven_days_ago),eq("topic_gensee_id",topic_gensee_id)))
                .projection(new Document("_id",1)).first();
        if(null != isExist) {
            Document isReply = topics_reply().find(and(gte("timestamp",seven_days_ago),eq("topic_reply_gensee_id",topic_reply_gensee_id)))
                    .projection(new Document("_id",1)).first();
            if(null == isReply) {
                String topic_id = isExist.getString("_id");
                saveResponseTopicReply(topicsGensee, topic_id, topic_reply_gensee_id);
            }
        } else {
            //0：kuaiji，1：zikao，2：xuelxuew
            int product = 0;
            if("zikao".equals(productName)) {
                product = 1;
            } else if("xuelxuew".equals(productName)) {
                product = 2;
            }
            //学员信息
            int stu_user_id = STU_USER_ID;
            String stu_user_pic = PIC_STUDENT;
            String stu_sso_userid = processUid(topicsGensee.get("submitter"), topicsGensee.get("name"), classplanLiveId).toString();
            if(null != stu_sso_userid) {
                Document user = users().find(eq("sso_userid", stu_sso_userid)).projection(new Document("_id", 1).append("pic",1)).first();
                if(null != user) {
                    stu_user_id = Integer.parseInt(user.get("_id").toString());
                    stu_user_pic = user.get("pic").toString();
                }
            }
            String topic_id = UUID.randomUUID().toString();
            saveTopic(topicsGensee, topic_id, topic_gensee_id, product, stu_sso_userid, stu_user_id);
            saveQuestionTopicReply(topicsGensee, topic_id, stu_sso_userid, stu_user_id, stu_user_pic);
            saveResponseTopicReply(topicsGensee, topic_id, topic_reply_gensee_id);
        }
    }

    /**
     * 保存问题到问题表
     * @param topicsGensee
     * @param topic_id
     * @param topic_gensee_id
     * @param product
     * @param stu_sso_userid
     * @param stu_user_id
     */
    private void saveTopic(Map<String,Object> topicsGensee, String topic_id, String topic_gensee_id, int product, String stu_sso_userid, int stu_user_id) {
        Document topic = new Document();
        topic.put("_id",topic_id);
        topic.put("topic_gensee_id",topic_gensee_id);
        topic.put("product",product);
        //提问管道类型 0.会答  1.题库  2.展示互动直播间
        topic.put("channel_type",2);
        topic.put("sso_userid",stu_sso_userid);
        topic.put("author_id",stu_user_id);
        topic.put("content",topicsGensee.get("question"));
        topic.put("timestamp",topicsGensee.get("submitTime"));
        //问题提交时间
        topic.put("submit_time",topicsGensee.get("submitTime"));
        topic.put("teach_id",TEA_USER_ID);
        //类型--> 0：待抢答 , 1：抢答失败 , 2：抢答成功 , 3：问题已结束
        topic.put("type",3);
        //评论状态--> 0：未评价 , 1：不满意 , 2：满意 , 3：很满意
        topic.put("evaluation_type",2);
        topic.put("tips",Arrays.asList(new Document("_id",0).append("tip_name","展示互动")));
        //更新时间
        topic.put("update_at",topicsGensee.get("submitTime"));
        //是否删除
        topic.put("deleted",false);
        //保存到数据库
        topics().insertOne(topic);
    }

    /**
     * 将问题内容保存到聊天表中
     * @param topicsGensee
     * @param topic_id
     * @param stu_sso_userid
     * @param stu_user_id
     * @param stu_user_pic
     */
    private void saveQuestionTopicReply(Map<String,Object> topicsGensee, String topic_id, String stu_sso_userid, int stu_user_id, String stu_user_pic) {
        Document question = new Document();
        question.put("_id",UUID.randomUUID().toString());
        question.put("reply_content",topicsGensee.get("question"));
        question.put("reply_time",topicsGensee.get("submitTime"));
        //0：文字, 1：图片, 2：语音,:3：系统文字, 4：红包;
        question.put("reply_type",0);
        question.put("topic_id",topic_id);
        question.put("sso_userid",stu_sso_userid);
        question.put("user_id",stu_user_id);
        question.put("user_pic",stu_user_pic);
        question.put("timestamp",topicsGensee.get("submitTime"));
        //0：所有人, 1：老师学员, 2：老师, 3：学员
        question.put("show_type",0);
        question.put("topics_reply_first",1);
        topics_reply().insertOne(question);
    }

    /**
     * 将回复内容保存到聊天表中
     * @param topicsGensee
     * @param topic_id
     * @param topic_reply_gensee_id
     */
    private void saveResponseTopicReply(Map<String,Object> topicsGensee, String topic_id, String topic_reply_gensee_id) {
        Document response = new Document();
        response.put("_id",UUID.randomUUID().toString());
        response.put("reply_content",topicsGensee.get("response"));
        response.put("reply_time",topicsGensee.get("answerTime"));
        //0：文字, 1：图片, 2：语音,:3：系统文字, 4：红包;
        response.put("reply_type",0);
        response.put("topic_id",topic_id);
        response.put("topic_reply_gensee_id",topic_reply_gensee_id);
        response.put("user_id",TEA_USER_ID);
        response.put("user_pic",PIC_TEACHER);
        response.put("timestamp",topicsGensee.get("answerTime"));
        //0：所有人, 1：老师学员, 2：老师, 3：学员
        response.put("show_type",0);
        response.put("topics_reply_first",1);
        topics_reply().insertOne(response);
    }

    /**
     * 提取uid
     */
    private Long processUid(Object obj, Object nickname_obj, String classplanLiveId){
        Integer uidI = null;
        Long uidL = null;
        try{
            uidI = Integer.parseInt(obj.toString());
            if(uidI > 1000000000){
                uidI -= 1000000000;
            }
            uidL = uidI.longValue();
        }catch(Exception e){
            uidL = Long.parseLong(obj.toString());
            //如果uid以99开头,即为展示互动给到的uid,即为缺失的数据,通过昵称获得uid
            if(uidL.toString().indexOf("99") == 0){
                List<Long> userIdList = courseUserplanDetailService.getUserIdByClassplanLiveId(classplanLiveId, (String)nickname_obj);
                if(null != userIdList && userIdList.size() > 0){
                    uidL = userIdList.get(0);
                    return uidL;
                }
            }
            uidL = uidL - 10000000000L;
        }
        return uidL;
    }
}
