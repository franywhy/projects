package com.hq.answerapi.dao.xyqquser;

import com.hq.answerapi.entity.QQUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author hq
 */
public interface QQUserRepository extends MongoRepository<QQUser, BigInteger> {

	List<QQUser> findByUsername(String username);

	List<QQUser> findByNickName(String nickName);

	List<QQUser> findByTuid(String tuid);

	List<QQUser> findByUsernameAndPassword(String username, String password);
	
	List<QQUser> findByPk(String pk);

}
