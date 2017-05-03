package dto;

public class Paypal {
	private int paypalID;
	
	private String account;
	
	private int ppuserID;

	public int getPaypalID() {
		return paypalID;
	}

	public void setPaypalID(int paypalID) {
		this.paypalID = paypalID;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getPpuserID() {
		return ppuserID;
	}

	public void setPpuserID(int ppuserID) {
		this.ppuserID = ppuserID;
	}
}
