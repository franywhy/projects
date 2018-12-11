package com.hqjy.msg.service.impl;

import com.hqjy.msg.mapper.MsgChannelsMapper;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.service.MsgChannelsService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgChannelsServiceImpl extends MsgChannelsService<MsgChannels> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setMsgChannelsMapper(MsgChannelsMapper msgChannelsMapper) {
        super.setBaseMapper(msgChannelsMapper);
    }

    //根据群组查询群组成员频道表信息
    public List<MsgChannels> findMsgChannelsByGroup(String groupChannel) {
        //根据群组查询
        Map map = new HashMap();
        map.put("groupChannel", groupChannel);

        List<MsgChannels> list = super.example(map, MsgChannels.class);
        return list;
    }

    @Override
    public List findMsgChannelsByGroupJdbc(String groupChannel) {
        if (groupChannel.contains(",")) {
            String [] str= groupChannel.split(",");
            List list = new ArrayList();
            for (int i = 0; i < str.length; i++) {
                list.add(str[i]);
            }

            String sql ="select leaguer_channel as `" + Constant.REDIS_KEY + "` from msg_channels where group_channel in ("+ListUtils.listToSqlIn(list)+");";
            return jdbcTemplate.queryForList(sql);
        }
        //"select leaguer_channel as `" + Constant.REDIS_KEY + "` from msg_channels where group_channel ='" + groupChannel.replaceAll("'", "") + "';"
    	String sql ="select leaguer_channel as `" + Constant.REDIS_KEY + "` from msg_channels where group_channel ='" + groupChannel.replaceAll("'", "") + "';";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public boolean isExistsByGroupAndLeaguer(String groupChannel, String leaguerChannel) {

        List list = jdbcTemplate.queryForList("select 1 from msg_channels where group_channel ='" + groupChannel.replaceAll("'", "") + "' and leaguer_channel = '" + leaguerChannel.replaceAll("'", "") + "';");
        if (null != list && !list.isEmpty()) 
        	return true;
        return false;
    }

}
