package com.hqjy.msg.service.impl;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.mapper.MsgChannelsMapper;
import com.hqjy.msg.mapper.MsgGroupMapper;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.service.MsgChannelsService;
import com.hqjy.msg.service.MsgGroupService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsgGroupServiceImpl extends MsgGroupService<MsgGroup> {

    @Autowired
    private MsgChannelsMapper msgChannelsMapper;
 
    @Autowired
    private MsgChannelsService msgChannelsService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setMsgGroupMapper(MsgGroupMapper msgGroupMapper) {
        super.setBaseMapper(msgGroupMapper);
    }

    /**
     * 判断群组表的信息
     * @param groupChannel 群组频道
     * @return
     */
    public boolean isExistsGroup(String groupChannel) {
        boolean isGroupExists = false;
        //1.先查询群组表信息  根据群组channnel查询
        Map map = new HashMap();
        map.put("channel", groupChannel);

        List<MsgGroup> list = super.example(map, MsgGroup.class);

        //2.如果群组信息不为空

        if (!list.isEmpty() && list != null) {

            //判断group是否存在
            isGroupExists = true;

        }
        return isGroupExists;
    }

    /**
     * 添加数据到消息表并且更新成员信息
     *
     * @param groupChannel
     * @param channelsJson
     * @return
     */
    public List insertMsgGroupAndLeaguer(String groupChannel, List channelsJson) {

        int i = 0;

        try {
            //新增群组表和群组成员信息
            MsgGroup mg = new MsgGroup();
            mg.setChannel(groupChannel);
            mg.setType(Constant.GROUP_TYPE_GROUP);  //0表示系统
            mg.setName(groupChannel); //名称
            mg.setRemark("");//描述
            i = super.getBaseMapper().insert(mg);

            List list = new ArrayList();
            Map map = null;

            for (Object string : channelsJson) {
                //创建群组成员对象
                MsgChannels m = new MsgChannels();
                m.setGroupChannel(groupChannel);
                m.setLeaguerChannel(string.toString());
                i = msgChannelsMapper.insert(m);

                //
                map = new HashMap();
                map.put(Constant.REDIS_KEY, string.toString());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void addMsgGroupAndLeaguer(String groupChannel, String leaguerChannel, int type) {
        try {
            //新增群组表和群组成员信息
            MsgGroup mg = new MsgGroup();
            mg.setChannel(groupChannel);
            mg.setType(type);  //0表示系统
            mg.setName(groupChannel); //名称
            mg.setRemark("");//描述
            super.getBaseMapper().insert(mg);
            //创建群组成员对象
            MsgChannels m = new MsgChannels();
            m.setGroupChannel(groupChannel);
            m.setLeaguerChannel(leaguerChannel);
            msgChannelsMapper.insert(m);
        } catch (Exception e) {
        }

    }

    /**
     * 验证群组表信息
     * @param groupChannels 群组频道

     * @return 返回值为string
     */
    public String checkGroup(List groupChannels) throws DefaultException {

        for (int i = 0; i < groupChannels.size(); i++) {
            String groupChannel = (String) groupChannels.get(i);
            if (!isExistsGroup(groupChannel)) {
                throw new DefaultException(groupChannel + "频道不存在");
            }

        }


        return null;
    }

    /**
     *
     * @param groupChannels 群组频道
     * @param channelsJson  成员频道
     * @return 返回值为list集合
     */
    @Override
    public List getGroupChildrens(List groupChannels, List channelsJson) {
        Map map = null;
        String sql = "select leaguer_channel as `" + Constant.REDIS_KEY + "`  from msg_channels where group_channel in (" + ListUtils.listToSqlIn(groupChannels) + ")  group by leaguer_channel";
        List list = jdbcTemplate.queryForList(sql);
        if (null == channelsJson || channelsJson.isEmpty()) {
            return list;
        }

        for (int i = 0; i < channelsJson.size(); i++) {
            map = new HashMap();
            map.put(Constant.REDIS_KEY, channelsJson.get(i));
            list.add(map);
        }
        return  list;
    }

    /**
     * 批量添加数据
     *
     * @param msgGroup
     * @return
     */
    public List<MsgGroup> addBatch(MsgGroup msgGroup) {
        String sql = "INSERT INTO msg_group VALUES(null," + msgGroup.getName() + "," + msgGroup.getChannel() + "," + msgGroup.getRemark() + "," + msgGroup.getType() + " ";
        jdbcTemplate.batchUpdate(sql);

        return null;
    }

	/**
	 * 更新群组信息
	 * @param userId 用户 ID
	 * @param groups 群体数组
	 * @return
	 * @throws DefaultException 
	 * @throws Exception 
	 */
	@Override
    @Transactional
	public int updateMsgGroupInfo(String userId, List groups)  {

	        //StringBuilder sbAdd =  new StringBuilder();
            //StringBuilder sbdel =  new StringBuilder();
            
            if (null!=groups && !groups.isEmpty()){
                for(int i =0;i <groups.size();i++){
                    Map map = (Map) groups.get(i);
                    String channel = (String) map.get("channel");
                    Integer type = ((Double) map.get("type")).intValue();

                    if(type==Constant.GROUP_ADD){
                        //判断群组channel是否存在，不存在才添加（msg_group）
                        if(!isExistsGroup(channel)){
                            //
                            MsgGroup group = new MsgGroup();
                            group.setName(channel);
                            group.setChannel(channel);
                            group.setType(Constant.GROUP_TYPE_USER);
                            int j = super.insert(group);
                            
                        }else{
                        	//throw new DefaultException("群组频道已存在，请不要重复添加！");
                        	
                        }
                        //判断成员的群组是否存在，不存在才添加（msg_channels）
                        if (!msgChannelsService.isExistsByGroupAndLeaguer(channel,userId)){
                            MsgChannels msgChannels = new MsgChannels();
                            msgChannels.setLeaguerChannel(userId);
                            msgChannels.setGroupChannel(channel);                       
                            int k = msgChannelsMapper.insert(msgChannels);
                            
                        }else{
                        	//throw new DefaultException(channel+"群组成员信息已存在，请不要重复添加！");
                        	
                        }
                    }
                    if(type==Constant.GROUP_DEL){
                        //批量删除成员频道
                    	String sql ="DELETE from msg_channels WHERE group_channel= ? and leaguer_channel=?";
						jdbcTemplate.update(sql,channel,userId);
                    }
                }
            }
						return  0 ;
	}


}






















