package dto;

public class User {

	private int id;

	private String loginID;

	private String name;

	private String email;

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
	public User(String loginID) {
		super();
		this.loginID = loginID;
	}

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
	public String getLoginID() {
		return loginID;
	}

	/**
	 * @param loginID
	 *            the loginID to set
	 */
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

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
