package io.renren.modules.job.dao.xy;

import io.renren.modules.job.entity.mongo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2018/9/19 0019.
 * @author hq
 */
public interface UserRepository extends MongoRepository<User, Long> {
}
