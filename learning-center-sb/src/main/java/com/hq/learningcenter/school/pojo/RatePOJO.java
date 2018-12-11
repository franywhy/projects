package com.hq.learningcenter.school.pojo;

import java.io.Serializable;

public class RatePOJO implements Serializable {

	private static final long serialVersionUID = 1814176868785654582L;
	
	private Integer progressRate;
	
	private Integer participationRate;
	
	private Integer completedRate;

	public Integer getProgressRate() {
		return progressRate;
	}

	public void setProgressRate(Integer progressRate) {
		this.progressRate = progressRate;
	}

	public Integer getParticipationRate() {
		return participationRate;
	}

	public void setParticipationRate(Integer participationRate) {
		this.participationRate = participationRate;
	}

	public Integer getCompletedRate() {
		return completedRate;
	}

	public void setCompletedRate(Integer completedRate) {
		this.completedRate = completedRate;
	}
	
	
}
