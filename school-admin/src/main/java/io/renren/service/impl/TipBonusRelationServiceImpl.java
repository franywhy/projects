package io.renren.service.impl;

import io.renren.entity.LogLabelBonusRelationRecordEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.manage.TipBonusRelationPOJO;
import io.renren.service.LogLabelBonusRelationRecordService;
import io.renren.service.SysUserLaberService;
import io.renren.service.TipBonusRelationService;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.ShiroUtils;
import io.renren.utils.http.HttpClientUtil4_3;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("tipBonusRelationService")
public class TipBonusRelationServiceImpl implements TipBonusRelationService {

	@Autowired
	private LogLabelBonusRelationRecordService logLabelBonusRelationRecordService;

	@Autowired
	private SysUserLaberService sysUserLaberService;

	@Value("#{application['kuaida.api']}")
	private String KUAIDA_API;

	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}


	@Override
	public Object queryObject(Long id) {
		String url = KUAIDA_API+"/api/tipBunusRelation/getTipBunusRelationByTipId";
		String params = "tid=" + id;
		String httpResponse = null;
		try {
			httpResponse = HttpUtils.sendGet(url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map tipMap = JSONUtil.jsonToMap(httpResponse);
		Object tipBonusRelationList = tipMap.get("data");
		if (((LinkedHashMap) tipBonusRelationList).size() == 0) {
			tipBonusRelationList = null;
		}
		return tipBonusRelationList;
	}

	@Override
	public List<TipBonusRelationPOJO> queryList(Map<String, Object> map) {
		return null;
	}

	@Override
	public List<Object> queryListObject(Map<String, Object> map) {
//		测试数据String aa = "{\"msg\":\"获取标签对应红包关系和对应红包限制列表\",\"code\":1,\"data\":[{\"min\":10,\"max\":11,\"createTime\":1535350859734,\"name\":\"会计实操\",\"veryMax\":22,\"veryMin\":21,\"tid\":100010},{\"min\":14,\"max\":15,\"createTime\":1535357492963,\"name\":\"职业成长\",\"veryMax\":18,\"veryMin\":17,\"tid\":100040}]}";
		String url = KUAIDA_API+"/api/tipBunusRelation/getTipBunusRelationList";
		Map<String, String> maps =new HashMap<>();
		maps.put("labelName",map.get("labelName")+"");
		maps.put("page",map.get("offset")+"");
		String httpResponse = null;
		try {
			httpResponse = HttpClientUtil4_3.sendHttpPost(url,maps);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map tipMap = JSONUtil.jsonToMap(httpResponse);
		List<Object> tipBonusRelationList = (List<Object>) tipMap.get("data");

		return tipBonusRelationList;
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		String url = KUAIDA_API+"/api/tipBunusRelation/queryTotal";
		Map<String, String> maps =new HashMap<>();
		maps.put("labelName",map.get("labelName")+"");
		String httpResponse = null;
		try {
			httpResponse = HttpClientUtil4_3.sendHttpPost(url,maps);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map tipMap = JSONUtil.jsonToMap(httpResponse);
		List<Object> tipBonusRelationList = (List<Object>) tipMap.get("data");

		return tipBonusRelationList.size();
	}

	@Override
	public void save(Map map) {
		Long userId = getUser().getUserId();
		String laberName = map.get("laberNames").toString();
		Double maxMoney = Double.parseDouble(map.get("maxMoney").toString());
		Double minMoney = Double.parseDouble(map.get("minMoney").toString());
		Double veryMaxMoney = Double.parseDouble(map.get("veryMaxMoney").toString());
		Double veryMinMoney = Double.parseDouble(map.get("veryMinMoney").toString());
		Integer laberId = (Integer)map.get("laberId");
		Long laberIdL=laberId.longValue();

		LogLabelBonusRelationRecordEntity logLabelBonusRelationRecordEntity = new LogLabelBonusRelationRecordEntity();
		logLabelBonusRelationRecordEntity.setCreator(userId);
		logLabelBonusRelationRecordEntity.setModifier(userId);
		logLabelBonusRelationRecordEntity.setLabelId(laberIdL);
		logLabelBonusRelationRecordEntity.setTipName(laberName);
		logLabelBonusRelationRecordEntity.setSatisyMoney(minMoney + "-" + maxMoney);
		logLabelBonusRelationRecordEntity.setVerySatisyMoney(veryMinMoney + "-" + veryMaxMoney);
		logLabelBonusRelationRecordService.save(logLabelBonusRelationRecordEntity);

		String url = KUAIDA_API+"/api/tipBunusRelation/saveTipBunusRelation";

		String params = "userId="+userId+"&product="+map.get("product")+"&tip_id="+map.get("laberId")+"&maxMoney="+maxMoney+"&minMoney="+minMoney+"&veryMaxMoney="+veryMaxMoney+"&veryMinMoney="+veryMinMoney;
		try {
			String httpResponse = HttpUtils.sendGet(url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Map map) {
		Long userId = getUser().getUserId();
		String laberName = map.get("laberNames").toString();
		Double maxMoney = Double.parseDouble(map.get("maxMoney").toString());
		Double minMoney = Double.parseDouble(map.get("minMoney").toString());
		Double veryMaxMoney = Double.parseDouble(map.get("veryMaxMoney").toString());
		Double veryMinMoney = Double.parseDouble(map.get("veryMinMoney").toString());
		Integer laberId = (Integer)map.get("laberId");
		Long laberIdL=laberId.longValue();

		LogLabelBonusRelationRecordEntity logLabelBonusRelationRecordEntity = new LogLabelBonusRelationRecordEntity();
		logLabelBonusRelationRecordEntity.setCreator(userId);
		logLabelBonusRelationRecordEntity.setModifier(userId);
		logLabelBonusRelationRecordEntity.setLabelId(laberIdL);
		logLabelBonusRelationRecordEntity.setTipName(laberName);
		logLabelBonusRelationRecordEntity.setSatisyMoney(minMoney + "-"+ maxMoney);
		logLabelBonusRelationRecordEntity.setVerySatisyMoney(veryMinMoney + "-"+ veryMaxMoney);
		logLabelBonusRelationRecordEntity.setModifyReason(map.get("modifyReason").toString());

		logLabelBonusRelationRecordService.save(logLabelBonusRelationRecordEntity);

		String url = KUAIDA_API+"/api/tipBunusRelation/updateTipBunusRelation";
		String params = "userId=" + userId + "&product=" + map.get("product") + "&tip_id=" + map.get("laberId") + "&maxMoney=" + maxMoney + "&minMoney=" + minMoney + "&veryMaxMoney=" + veryMaxMoney + "&veryMinMoney=" + veryMinMoney;
		try {
			String httpResponse = HttpUtils.sendGet(url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean queryPower(Long laberIdL) {
		Long userId = getUser().getUserId();
		String laberString = sysUserLaberService.queryLaberByUserId(userId);
		List<String> inputMenuIds = Arrays.asList(laberString.split(","));
		List<Long> menuIdList = new ArrayList<Long>();
		CollectionUtils.collect(inputMenuIds, new Transformer() {
			@Override
			public Object transform(Object o) {
				return Long.valueOf(o.toString());
			}
		}, menuIdList);
		Boolean resule=menuIdList.contains(laberIdL);
		return resule;
	}
}
