package dto;

public class Solution {
	private int solutionID;
	
	private String solutionContent;
	
	private int userID;
	
	private String location;
	
	private String date;
	
	private boolean isSelected;

	public int getSolutionID() {
		return solutionID;
	}

	public void setSolutionID(int solutionID) {
		this.solutionID = solutionID;
	}

	public String getSolutionContent() {
		return solutionContent;
	}

	public void setSolutionContent(String solutionContent) {
		this.solutionContent = solutionContent;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
