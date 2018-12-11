package com.elise.datacenter.dao;


import com.elise.datacenter.entity.FileEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface FileDao {

    FileEntity queryObject(String fileId);

    Integer create(FileEntity entity);

    Integer deleteById(String fileId);
}
