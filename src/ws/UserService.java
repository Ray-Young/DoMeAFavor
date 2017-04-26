package ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import dao.UserDAO;
import dto.User;

@Path("/user")
public class UserService {

	// @GET
	// @Path("/getUser")
	// @Produces("application/json")
	// // @Produces(MediaType.APPLICATION_JSON)
	// public String getUserById(@QueryParam("name") String name) throws
	// Exception {
	// String users = null;
	// UserDAO dao = new UserDAO();
	// ArrayList<User> datas = dao.getUsers(name);
	// Gson gson = new Gson();
	// users = gson.toJson(datas);
	//
	// return users;
	// }

	@GET
	@Path("/getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUser() throws Exception {
		// String users = null;
		// UserDAO dao = new UserDAO();
		// ArrayList<User> datas = dao.getUsers();
		// Gson gson = new Gson();
		// users = gson.toJson(datas);
		User users = new User();
		users.setName("aaa");
		Gson gson = new Gson();
		System.out.println("aa" + gson.toJson(users));
		return gson.toJson(users);
	}

	@POST
	@Path("/createUser")
	@Consumes("application/x-www-form-urlencoded")
	public String createUser(@FormParam("name") String name) throws Exception {
		UserDAO dao = new UserDAO();
		dao.createUser(name);

		return "Success";
	}

}
