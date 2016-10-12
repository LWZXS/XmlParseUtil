package com.csg.xmlprase.test;

public class AbroadApprove {

	private String account;
	private String firstApprove;
	private String secondApprove;
	private String thirdApprove;
	private String fourthApprove;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFirstApprove() {
		return firstApprove;
	}

	public void setFirstApprove(String firstApprove) {
		this.firstApprove = firstApprove;
	}

	public String getSecondApprove() {
		return secondApprove;
	}

	public void setSecondApprove(String secondApprove) {
		this.secondApprove = secondApprove;
	}

	public String getThirdApprove() {
		return thirdApprove;
	}

	public void setThirdApprove(String thirdApprove) {
		this.thirdApprove = thirdApprove;
	}

	public String getFourthApprove() {
		return fourthApprove;
	}

	public void setFourthApprove(String fourthApprove) {
		this.fourthApprove = fourthApprove;
	}

	@Override
	public String toString() {
		return "account: " + this.account + "\nfirstApprove: "
				+ this.firstApprove + "\nsecondApprove: " + this.secondApprove
				+ "\nthirdApprove: " + this.thirdApprove + "\nfourthApprove: "
				+ this.fourthApprove;
	}

}
