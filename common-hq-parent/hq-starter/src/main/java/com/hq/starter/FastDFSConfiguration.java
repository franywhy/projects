package com.hq.starter;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * Created by Glenn on 2017/5/18 0018.
 */

@Configuration("FastDFSConfig")
@EnableConfigurationProperties(FastDFSRepository.class)
@EnableCaching
@ConditionalOnProperty(prefix = "fdfs",value = "enabled",havingValue = "true")
public class FastDFSConfiguration {

    private  final FastDFSRepository fastDFSRepository;

    public FastDFSConfiguration(FastDFSRepository fastDFSRepository){
        this.fastDFSRepository = fastDFSRepository;
    }

    @Bean
    @ConditionalOnClass({ClientGlobal.class,TrackerClient.class})
    public TrackerClient getTrackerClient() throws MyException {
        String[] parts;
        ClientGlobal.setG_anti_steal_token(fastDFSRepository.getAntiStealToken());
        ClientGlobal.setG_charset(fastDFSRepository.getCharset());
        ClientGlobal.setG_connect_timeout(fastDFSRepository.getConnectTimeout()*1000);
        ClientGlobal.setG_network_timeout(fastDFSRepository.getNetworkTimeout()*1000);
        ClientGlobal.setG_secret_key(fastDFSRepository.getSecretKey());
        InetSocketAddress[] tracker_servers = new InetSocketAddress[fastDFSRepository.getTrackerServer().size()];
        for (int i=0; i<fastDFSRepository.getTrackerServer().size(); i++)
        {
            parts =fastDFSRepository.getTrackerServer().get(i).split("\\:", 2);
            if (parts.length != 2)
            {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
        ClientGlobal.setG_tracker_http_port(fastDFSRepository.getTrackerHttpPort());
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient;
    }
}
