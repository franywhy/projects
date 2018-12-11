package com.hq.adaptive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlPhaseFilesDao;
import com.hq.adaptive.entity.AdlPhaseFilesEntity;
import com.hq.adaptive.service.AdlPhaseFilesService;
@Service("adlPhaseFilesService")
public class AdlPhaseFilesServiceImpl implements AdlPhaseFilesService {
	@Autowired
	private AdlPhaseFilesDao adlPhaseFilesDao;
	
	@Override
	public int save(Long phaseId,Long phaseType,AdlPhaseFilesEntity adlPhaseFilesEntity) {
        if(adlPhaseFilesEntity.getFileName() != null && !adlPhaseFilesEntity.getFileName().equals("")
                && adlPhaseFilesEntity.getFileUrl() != null && !adlPhaseFilesEntity.getFileUrl().equals("")){
            adlPhaseFilesEntity.setPhaseId(phaseId);
            adlPhaseFilesEntity.setPhaseType(phaseType);
            return adlPhaseFilesDao.save(adlPhaseFilesEntity);
        }
	    return 0;
	}

	@Override
	public AdlPhaseFilesEntity queryObject(Long phaseId, Long phaseType) {
		AdlPhaseFilesEntity adlPhaseFilesEntity = adlPhaseFilesDao.queryObject(phaseId,phaseType);
		if( adlPhaseFilesEntity == null){
			adlPhaseFilesEntity = new AdlPhaseFilesEntity();
		}
		return adlPhaseFilesEntity;
	}

	@Override
	public void deleteByPhaseId(Long phaseId) {
		adlPhaseFilesDao.deleteByPhaseId(phaseId);
	}

}
