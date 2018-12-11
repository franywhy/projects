package com.hqjy.msg.service;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgGroup;

import java.util.List;

/**
 * 群组表接口
 * @author Administrator
 *
 */
public abstract class MsgGroupService<T> extends  BaseService<T>{

	/**
	 * 判断群组是否存在
	 * @param groupChannel  群组频道
	 * @return
	 */
	public abstract boolean isExistsGroup(String groupChannel);

	/**
	 * 添加群组数据
	 * @param groupChannel
	 * @param channelsJson
	 * @return
	 */
	public abstract List insertMsgGroupAndLeaguer(String groupChannel,List channelsJson);

	/**
	 * 添加数据
	 * @param groupChannel
	 * @param leaguerChannel
	 * @param type
	 * @return
	 */
	public abstract void addMsgGroupAndLeaguer(String groupChannel,String leaguerChannel,int type);

	/**
	 * 检查是否存在
	 * @param groupChannel
	 * @return
	 */
	public abstract String checkGroup(List groupChannel) throws DefaultException;

	/**
	 * 寻找成员
	 * @param groupChannel
	 * @param channelsJson
	 * @return
	 */
	public abstract List getGroupChildrens(List groupChannel,List channelsJson);

	/**
	 * 批量添加数据
	 * @param msgGroup
	 * @return
	 */
	public abstract List<MsgGroup> addBatch(MsgGroup msgGroup);

	/**
	 * 更新群组信息
	 * @param userId 用户 ID
	 * @param groups 群体数组
	 * @return
	 */
	public abstract int updateMsgGroupInfo(String userId,List groups) ;

}
