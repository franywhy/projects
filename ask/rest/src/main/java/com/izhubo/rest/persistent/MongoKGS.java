package com.izhubo.rest.persistent;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.izhubo.rest.common.doc.MongoKey;

/**
 * date: 13-6-20 下午2:36
 *
 * @author: wubinjie@ak.cc
 */
public class MongoKGS implements KGS{


    static final String COLL_NAME = "counters";
    static final String FIELD = "seq";

    public void setDb(DB db) {
        this.collection = db.getCollection(COLL_NAME);
    }

    private DBCollection collection;


    public int nextId() {
        return nextId(FIELD);
    }

    private static final DBObject seqField = new BasicDBObject(FIELD,1);
    private static final DBObject incSeq = new BasicDBObject(MongoKey.$inc,new BasicDBObject(FIELD,1));

    public Integer nextId(String nameSpace) {
        return (Integer)collection.findAndModify(new BasicDBObject(MongoKey._id,nameSpace),
                seqField,MongoKey.NO_SORT,false,incSeq,true,true).get(FIELD);
    }
}
