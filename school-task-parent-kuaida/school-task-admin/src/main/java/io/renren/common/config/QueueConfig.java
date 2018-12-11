package io.renren.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
/**
 * @author linchaokai
 * @Description
 * @date 2018/5/14 10:57
 */
@Configuration
public class QueueConfig {
    public static final String QUEUE_NAME = "dev.kj.class.sync.tk";

    @Bean(name="dev.kj.class.sync.tk")
    public Queue queue() {
        return new Queue(QueueConfig.QUEUE_NAME);
    }
}
