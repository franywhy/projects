package com.hqjy.msg.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.hqjy.msg.enumeration.DbContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by baobao on 2017/12/20 0020.
 * 数据库的工具类
 */
public class DatabaseUtils {
    private static Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);

    /**
     * 根据数据库数组，随机获取一个数据库的值
     * @param strs 数据库名称的数组
     * @return
     */
    private  static String getDatabase(List<String> strs ){

        Random random = new Random();
        int size = random.nextInt(strs.size());
        String str = strs.get(size);
        logger.info("random:"+str);
        DruidDataSource ds = null;
        try{
            ds = (DruidDataSource) SpringUtils.getBean(str);
        }catch (Exception ex){
            ds = null;
        }

        try {
            if(!WebPingUtils.testMysqlConn(ds.getUrl())){
                logger.info("切换中....");
                ds.close();
                strs.remove(size);
                return getDatabase(strs);
            }

        } catch (Exception e1) {

            strs.remove(size);
            ds.close();
            return getDatabase(strs);
        }
        return str ;
    }

    /**
     * 获取从区（读区）的数据库（mybatis）
     * @return
     */
    public static DbContextHolder.SLAVE chooseSlaveDatabase(){
        DbContextHolder.SLAVE[] slaves = DbContextHolder.SLAVE.values();

        List<String> strs = new ArrayList();
        for (int i = 0; i< slaves.length;i++) {
            strs.add( slaves[i].getValue());
        }
        String str = DatabaseUtils.getDatabase(strs);
        for (int i = 0; i< slaves.length;i++) {
           if(str ==slaves[i].getValue())
           {
               return slaves[i];
           }
        }
        return  chooseSlaveDatabase();


    }
    /**
     * 获取从区（读区）的数据库（jdbc）
     * @return
     */
    public static void setSlaveJdbcTemplate(Class clz) throws Exception{
         //Field field =  classes.getDeclaredField("jdbcTemplate");
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {// --for() begin
            if(field.getType() == JdbcTemplate.class){
                logger.info(field.getName()+" is JdbcTemplate");

                JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtils.getBean(field.getName());
                DbContextHolder.SLAVE[] slaves = DbContextHolder.SLAVE.values();
                List<String> strs = new ArrayList();
                for (int i = 0; i< slaves.length;i++) {
                    strs.add( slaves[i].getValue());
                }
                String str = DatabaseUtils.getDatabase(strs);
                //
                if(null!=str && !str.equals("")){
                    jdbcTemplate.setDataSource((DataSource) SpringUtils.getBean(str));
                }


            }
        }


    }
}
