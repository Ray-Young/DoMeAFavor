package dto;

public class Facebook {
	private int FBID;
	
	private String name;
	
	private String account;
	
	private int userID;

	public int getFBID() {
		return FBID;
	}

	public void setFBID(int fBID) {
		FBID = fBID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	
}
