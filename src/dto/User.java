package dto;

public class User {

	private int id;

	private String fbToken;

	private String name;

	private String email;
	
	private int credit;
	
	private String fbPhoto;

	public String getFbPhoto() {
		return fbPhoto;
	}

	public void setFbPhoto(String fbPhoto) {
		this.fbPhoto = fbPhoto;
	}

	public String getFBToken() {
		return fbToken;
	}

	public void setFBToken(String fbToken) {
		this.fbToken = fbToken;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	private boolean active;

	/**
	 * Default constructor to create new user
	 */
	public User() {
		super();
	}

	/**
	 * Create new user with user name
	 *
	 * @param name
	 *            the login ID
	 */
//	public User(String loginID) {
//		super();
//		this.loginID = loginID;
//	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the loginID
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
