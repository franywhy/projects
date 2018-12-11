package com.hqjy.msg.config.mysql;


import com.hqjy.msg.util.DatabaseUtils;
import com.hqjy.msg.enumeration.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by baobao on 2017/12/12 0012.
 */
public class ReadWriteSplitRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DbContextHolder.DbType key = DbContextHolder.getDbType();
        if (key .equals(DbContextHolder.DbType.MASTER)){
            return DbContextHolder.DbType.MASTER;
        }

        return DatabaseUtils.chooseSlaveDatabase();
    }
}