package model;


import java.util.Date;

public class McPatent extends EntityUtil {

	public Long renewalId;
	public Long patentId;
	public String  renewalStatus;
	public String  epoPatentStatus;
	public Date lastRenewedDateExEpo;  // Do we want this ???

	
	
	
	public Long getRenewalId() {
		return renewalId;
	}
	public void setRenewalId(Long renewalId) {
		this.renewalId = renewalId;
	}
	public Long getPatentId() {
		return patentId;
	}
	public void setPatentId(Long patentId) {
		this.patentId = patentId;
	}
	public String getRenewalStatus() {
		return renewalStatus;
	}
	public void setRenewalStatus(String renewalStatus) {
		this.renewalStatus = renewalStatus;
	}
	public String getEpoPatentStatus() {
		return epoPatentStatus;
	}
	public void setEpoPatentStatus(String epoPatentStatus) {
		this.epoPatentStatus = epoPatentStatus;
	}
	public Date getLastRenewedDateExEpo() {
		return lastRenewedDateExEpo;
	}
	public void setLastRenewedDateExEpo(Date lastRenewedDateExEpo) {
		this.lastRenewedDateExEpo = lastRenewedDateExEpo;
	}

	
}
