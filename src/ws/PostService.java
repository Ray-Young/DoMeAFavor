package ws;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import dao.PostDAO;
import dto.Message;
import dto.Post;

@Path("/post")
public class PostService {
	@GET
	@Path("/getOverview")
	@Produces("application/json")
	public String getOverview() throws Exception {
		String posts = null;
		PostDAO dao = new PostDAO();
		ArrayList<Post> data = dao.getOverview();
		Gson gson = new Gson();
		posts = gson.toJson(data);
		return posts;

	}

	@POST
	@Path("/addPost")
	@Consumes("application/x-www-form-urlencoded")
	public String addPost(@FormParam("title") String title, @FormParam("postContent") String postContent,
			@FormParam("location") String location, @FormParam("date") String date, @FormParam("reward") int reward,
			@FormParam("userID") int userID, @FormParam("name") String name, @FormParam("image") String image)
			throws Exception {
		System.out.println(title);
		System.out.println(postContent);
		System.out.println(location);
		System.out.println(date);
		System.out.println(reward);
		System.out.println(userID);
		System.out.println(name);
		// System.out.println(image);
		// System.out.println(image.length());
		PostDAO dao = new PostDAO();

		if (image.length()>0) {
			// String base64Image = image.split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(image);
//			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
//
//			String time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
//			String filename = time + ".jpg";
//			File outputfile = new File(filename);
//			ImageIO.write(img, "jpg", outputfile);
//
//			File im = new File(filename);

			dao.addPost(title, postContent, location, date, reward, userID, name, imageBytes);
		} else {
			dao.addPost(title, postContent, location, date, reward, userID, name, null);
		}

		// FileInputStream fis = new FileInputStream ( im );

		// ByteArrayOutputStream os = new ByteArrayOutputStream();
		// ImageIO.write(img, "jgp", os);
		// InputStream is = new ByteArrayInputStream(os.toByteArray());

		// ImageIcon icon=new ImageIcon(img);
		// JFrame frame=new JFrame();
		// frame.setLayout(new FlowLayout());
		// frame.setSize(200,300);
		// JLabel lbl=new JLabel();
		// lbl.setIcon(icon);
		// frame.add(lbl);
		// frame.setVisible(true);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		return "";
	}

	@GET
	@Path("/getDetail")
	@Produces("application/json")
	public String getDetail(@QueryParam("postID") int postID) throws Exception {
		System.out.println(postID + " This is postID from service");
		String detail = null;
		PostDAO dao = new PostDAO();
		Post post = dao.getDetail(postID);
		Gson gson = new Gson();
		detail = gson.toJson(post);
		return detail;
	}

	@GET
	@Path("/getMessage")
	@Produces("application/json")
	public String getMessage(@QueryParam("postID") int postID) throws Exception {
		System.out.println("PostID received by getMessage: " + postID);
		String messages = null;
		PostDAO dao = new PostDAO();
		ArrayList<Message> datas = dao.getMessage(postID);
		Gson gson = new Gson();
		messages = gson.toJson(datas);
		return messages;
	}

	@POST
	@Path("/addMessage")
	@Consumes("application/x-www-form-urlencoded")
	public String addMessage(@FormParam("date") String date, @FormParam("content") String content,
			@FormParam("senderID") int senderID, @FormParam("receiverID") int receiverID,
			@FormParam("postID") int postID) throws Exception {
		System.out.println("date is: " + date);
		System.out.println("content is: " + content);
		System.out.println("Sender ID is: " + senderID);
		System.out.println("Receiver ID is: " + receiverID);
		System.out.println("postID is: " + postID);
		PostDAO dao = new PostDAO();
		dao.addMessage(date, content, senderID, receiverID, postID);
		return "Add User Success!!!";
	}

	@GET
	@Path("/getMyPost")
	@Produces("application/json")
	public String getMyPost(@QueryParam("userID") int userID) throws Exception {
		String posts = null;
		System.out.println("userID received is: " + userID);
		PostDAO dao = new PostDAO();
		ArrayList<Post> datas = dao.getMyPost(userID);
		Gson gson = new Gson();
		posts = gson.toJson(datas);
		return posts;

	}

	@GET
	@Path("/offer")
	@Produces("application/json")
	public String offer(@QueryParam("messageID") int messageID) throws Exception {
		System.out.println("message get selected is: " + messageID);
		PostDAO dao = new PostDAO();
		int postID = dao.offer(messageID);
		String result ="post "+postID+" becomes invisible now"; 
		return result;
	}
	
	
	@GET
	@Path("/cancel")
	@Produces("application/json")
	public String cancel(@QueryParam("postID")int postID) throws Exception{
		
		System.out.println("post you wanna cancel is: "+postID);
		PostDAO dao = new PostDAO();
		dao.cancel(postID);
		return "post "+postID+" becomes visible again";
	}

}
