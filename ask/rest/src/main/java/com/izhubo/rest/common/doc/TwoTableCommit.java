package com.izhubo.rest.common.doc;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * Mongo 2table Commit.
 *
 *  1. Must Support Redo .
 *  2. USE crontab To check Redo .
 *
 * date: 13-5-20 下午1:07
 *
 * @author: wubinjie@ak.cc
 */
public interface TwoTableCommit {

    DBCollection main();

    /**
     * main field_name must not use this NAME.
     */
    DBCollection logColl();

    BasicDBObject queryWithId();

    BasicDBObject update();

    boolean successCallBack();

    void rollBack();

}
