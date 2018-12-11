package com.hqjy.msg.service;

import com.hqjy.msg.model.MsgChannels;

import java.util.List;

/**
 * 
 * @author Administrator
 * 
 */
public abstract class MsgChannelsService<T> extends  BaseService<T> {


	/**
	 * 根据群组频道查询群组成员
	 * @param groupChannel
	 * @return
	 */
	public abstract List<MsgChannels> findMsgChannelsByGroup(String groupChannel);

	/**
	 * 根据群组频道查询群组成员
	 * @param groupChannel
	 * @return
	 */
	public abstract List findMsgChannelsByGroupJdbc(String groupChannel);

	/**
	 * 查询群组频道跟群组成员是否存在
	 * @param groupChannel
	 * @return
	 */
	public abstract boolean isExistsByGroupAndLeaguer(String groupChannel,String leaguerChannel);


	
}
