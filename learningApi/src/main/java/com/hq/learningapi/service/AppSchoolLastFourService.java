package com.hq.learningapi.service;

import java.util.List;
import java.util.Map;

import com.hq.learningapi.pojo.AppSchoolPOJO;

public interface AppSchoolLastFourService {

	List<AppSchoolPOJO> querySchoolList(String businessId, double longitude, double latitude);

}
