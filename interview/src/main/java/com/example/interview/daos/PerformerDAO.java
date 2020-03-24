package com.example.interview.daos;

public class PerformerDAO {
	Integer performerId;
	String performer;
	
	public PerformerDAO() {}
	
	public PerformerDAO(Integer performerId, String performer) {
		super();
		this.performerId = performerId;
		this.performer = performer;
	}

	public Integer getPerformerId() {
		return performerId;
	}
	public void setPerformerId(Integer performerId) {
		this.performerId = performerId;
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
}
