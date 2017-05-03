package ws;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import dao.UserDAO;
import dto.User;

@Path("/user")
public class UserService {

	 @GET
	 @Path("/getUser")
	 @Produces("application/json")
	 // @Produces(MediaType.APPLICATION_JSON)
	 public String getUserByToken(@QueryParam("fbToken") String fbToken) throws
	 Exception {
	 String users = null;
	 UserDAO dao = new UserDAO();
	 ArrayList<User> datas = dao.getUsers(fbToken);
	 Gson gson = new Gson();
	 users = gson.toJson(datas);
	 System.out.println("User Service ---" + datas.get(0).getName());
	 return users;
	 }
	 
	@GET
	@Path("/getUser2")
	@Produces("application/json")
	public String getUserById(@QueryParam("userID") int userID) throws Exception {
		String user = null;
		UserDAO dao = new UserDAO();
		User data = dao.getUser2(userID);
		Gson gson = new Gson();
		user = gson.toJson(data);
		// System.out.println("User Service ---" + datas.get(0).getName());
		return user;
	}

//	@GET
//	@Path("/getUser")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String getUser() throws Exception {
//		// String users = null;
//		// UserDAO dao = new UserDAO();
//		// ArrayList<User> datas = dao.getUsers();
//		// Gson gson = new Gson();
//		// users = gson.toJson(datas);
//		UserDAO dao = new UserDAO();
//		ArrayList<User> datas = dao.getUsers();
//
//		Gson gson = new Gson();
//		//datas.get(0).setName("abc");
//		System.out.println(datas.get(0).getName());
//		System.out.println(datas);
//		return gson.toJson(datas);
//		
//	}

	@GET
	@Path("/createUser")
	@Produces("application/json")
	public String createUser(@QueryParam("name") String name, 
			@QueryParam("email") String email, 
			@QueryParam("token") String token) throws Exception {
		System.out.println(name + "," + email +"," + token+"-------------");
		UserDAO dao = new UserDAO();
		dao.createUser(name, email, token);

		return "Success";
	}
	
	@POST
	@Path("/addUser")
	@Consumes("application/x-www-form-urlencoded")
	public String addUser(@FormParam("name")String name,
			@FormParam("email")String email,
			@FormParam("fbToken")String fbToken,
			@FormParam("fbPhoto")String fbPhoto){
		
		UserDAO dao = new UserDAO();
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(fbPhoto);
		System.out.println(imageBytes);
		String result = null;
		try {
			result = dao.addUser(name, email, fbToken, imageBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
			

}
