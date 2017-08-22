package model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class McTransaction extends EntityUtil {

	public Long transactionId;
	public String latestTransStatus; 
	public Date lastUpdatedDate;
	public String P3S_TransRef;
	public Date transStartDate;
	public Date transTargetEndDate;
	public BigDecimal transAmount_USD;
	public List<McPatent> patents = new ArrayList<McPatent>();
	public int numPatents = 0;
	
	
	
	
	public String getTransactionId() {
		return transactionId.toString();
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getLatestTransStatus() {
		return latestTransStatus;
	}
	public void setLatestTransStatus(String latestTransStatus) {
		this.latestTransStatus = latestTransStatus;
	}
	public String getLastUpdatedDate() {
		return fmt(lastUpdatedDate);
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getP3S_TransRef() {
		return P3S_TransRef;
	}
	public void setP3S_TransRef(String p3s_TransRef) {
		P3S_TransRef = p3s_TransRef;
	}
	public String getTransStartDate() {
		return fmt(transStartDate);
	}
	public void setTransStartDate(Date transStartDate) {
		this.transStartDate = transStartDate;
	}
	public String getTransTargetEndDate() {
		return fmt(transTargetEndDate);
	}
	public void setTransTargetEndDate(Date transTargetEndDate) {
		this.transTargetEndDate = transTargetEndDate;
	}
	public String  getTransAmount_USD() {
		return transAmount_USD.toString();
	}
	public void setTransAmount_USD(BigDecimal transAmount_USD) {
		this.transAmount_USD = transAmount_USD;
	}
	public List<McPatent> getPatents() {
		return patents;
	}
	public void setPatents(List<McPatent> patents) {
		this.patents = patents;
	}

	
	
	
	
}
