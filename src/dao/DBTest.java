package dao;

public class DBTest {
	public static void main(String[] args) throws Exception {
		// CVCUserDAO dao = new CVCUserDAO();
		// ArrayList<User> datas = dao.getUsers();
		// for (User data : datas) {
		// System.out.println(data.getId());
		// System.out.println(data.getName());
		// System.out.println(data.getLoginID());
		// System.out.println(data.getEmail());
		// }
		UserDAO dao = new UserDAO();
		System.out.println(dao.getUsers("lei").get(0).getName());
		// dao.getUsers();
	}

}
