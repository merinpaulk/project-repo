package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class McOrders {
	
	public String p3sRef;
	
	public BigDecimal UsdToEurRate;
	
	public BigDecimal USDAmount;
	
	public BigDecimal EURAmount;
	
	public Date transStartDate;
	
	public Date transTargetEndDate;
	
	public String destination;
	
	public String speed;
	
	public Date nowDate;
	
	

	public String getP3sRef() {
		return p3sRef;
	}

	public void setP3sRef(String p3sRef) {
		this.p3sRef = p3sRef;
	}

	public BigDecimal getUsdToEurRate() {
		return UsdToEurRate;
	}

	public void setUsdToEurRate(BigDecimal usdToEurRate) {
		UsdToEurRate = usdToEurRate;
	}

	public BigDecimal getUSDAmount() {
		return USDAmount;
	}

	public void setUSDAmount(BigDecimal uSDAmount) {
		USDAmount = uSDAmount;
	}

	public BigDecimal getEURAmount() {
		return EURAmount;
	}

	public void setEURAmount(BigDecimal eURAmount) {
		EURAmount = eURAmount;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Date getNowDate() {
		return nowDate;
	}

	public void setNowDate(Date nowDate) {
		this.nowDate = nowDate;
	}
	
	

	public Date getTransStartDate() {
		return transStartDate;
	}

	public void setTransStartDate(Date transStartDate) {
		this.transStartDate = transStartDate;
	}

	public Date getTransTargetEndDate() {
		return transTargetEndDate;
	}

	public void setTransTargetEndDate(Date transTargetEndDate) {
		this.transTargetEndDate = transTargetEndDate;
	}

	
	

	public McOrders(String p3sRef, BigDecimal usdToEurRate, BigDecimal uSDAmount, BigDecimal eURAmount,
			Date transStartDate, Date transTargetEndDate, String destination, String speed, Date nowDate) {
		super();
		this.p3sRef = p3sRef;
		UsdToEurRate = usdToEurRate;
		USDAmount = uSDAmount;
		EURAmount = eURAmount;
		this.transStartDate = transStartDate;
		this.transTargetEndDate = transTargetEndDate;
		this.destination = destination;
		this.speed = speed;
		this.nowDate = nowDate;
	}

	public McOrders() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
