package com.hqjy.msg.enumeration;

/**
 * Created by Administrator on 2017/12/12 0012.
 */
public class DbContextHolder {
    public enum DbType{
        MASTER("master"),SLAVE("slave");
        private String value;
        DbType(String s) {
            this.value = s;
        }
        public String getValue() {
            return value;
        }
    }
    public enum SLAVE {
       ONE("slaveDataSource-01"),TWO("slaveDataSource-02");
        private String value;
        SLAVE(String s) {
            this.value = s;
        }
        public String getValue() {
            return value;
        }

    }
    public enum MASTER {
        ONE("masterDataSource-01"),TWO("masterDataSource-02");
        private String value;
        MASTER(String s) {
            this.value = s;
        }
        public String getValue() {
            return value;
        }
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<DbType>();

    public static void setDbType(DbType dbType){
        if(dbType==null)throw new NullPointerException();
        contextHolder.set(dbType);
    }

    public static DbType getDbType(){
        return contextHolder.get()==null? DbType.MASTER:contextHolder.get();
    }

    public static void clearDbType(){
        contextHolder.remove();
    }
}
