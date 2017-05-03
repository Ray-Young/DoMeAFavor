package dao;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.commons.io.*;
import javax.imageio.ImageIO;

//import com.sun.jersey.core.util.Base64;
import java.util.Base64;

import dto.Message;
import dto.Post;
import dto.User;

public class PostDAO {
	private Connection conn;
	
	public ArrayList<Post> getOverview() throws Exception{
		conn = new JDBC().getConn();
		String sql = "select * from post where isSolved=false;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<Post> datas = new ArrayList<Post>();
		while (result.next()) {

			Post data = new Post();
			data.setTitle(result.getString("title"));
			data.setPostID(result.getInt("postID"));
			data.setLocation(result.getString("location"));
			data.setDate(result.getString("date"));
			data.setPostContent(result.getString("postContent"));
			data.setReward(result.getInt("reward"));
			data.setUserID(result.getInt("userID"));
			data.setName(result.getString("name"));
			data.setSolved(result.getBoolean("isSolved"));
			data.setAttachments("");
			
			datas.add(data);
		}
		stmt.close();
		conn.close();
		return datas;
	}
	
	
	public void addPost(
			String title,
			String postContent,
			String location,
			String date,
			int reward,
			int userID,
			String name,
			byte[] imageBytes) throws Exception{
		conn = new JDBC().getConn();
		String sql="insert into post (title,postContent,location,date,reward,userID,name,attachments,isSolved) values (?,?, ?, ?, ?, ?, ?, ?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		System.out.println("Here");
		stmt.setString(1, title);
		stmt.setString(2,postContent);
		stmt.setString(3, location);
		stmt.setString(4, date);
		stmt.setInt(5, reward);
		stmt.setInt(6, userID);
		stmt.setString(7, name);
		if(imageBytes!=null){
			Blob blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
			stmt.setBlob(8, blob);
		}else{
			stmt.setBinaryStream(8, null, 0);
		}
		stmt.setBoolean(9, false);
		stmt.execute();
		stmt.close();
		conn.close();
		
	}
	
	public Post getDetail(int postID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "select * from post where postID="+postID;
		System.out.println(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		result.next();
		Post post = new Post();
		post.setPostID(postID);
		post.setTitle(result.getString("title"));
		post.setPostContent(result.getString("postContent"));
		post.setLocation(result.getString("location"));
		post.setDate(result.getString("Date"));
		post.setReward(result.getInt("reward"));
		post.setUserID(result.getInt("userID"));
		post.setSolved(result.getBoolean("isSolved"));
		InputStream is = result.getBinaryStream("attachments");
		System.out.println(is==null);
		
		if(is!=null){

			byte[] imageBytes = IOUtils.toByteArray(is);
 
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            post.setAttachments(base64String);
            System.out.println("here detial");
            
		}else{
			System.out.println("hehe no image");
			post.setAttachments("no image");
		}
		stmt.close();
		conn.close();
		return post;
	}
	
	public ArrayList<Message> getMessage(int postID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "select * from message where postID="+postID;
		System.out.println(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<Message> datas = new ArrayList<Message>();
		while(result.next()){
			
			Message message = new Message();
			message.setContent(result.getString("content"));
			//message.setTitle(result.getString("title"));
			message.setMessageID(result.getInt("messageID"));
			message.setPostID(result.getInt("postID"));
			message.setSenderID(result.getInt("senderID"));
			message.setReceiverID(result.getInt("receiverID"));
			message.setDate(result.getString("date"));
			
			datas.add(message);
			
		}
		stmt.close();
		conn.close();
		return datas;
	}
	
	public void addMessage(String date, String content, int senderID, int receiverID, int postID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "insert into message (content,senderID,receiverID,postID,date) values(?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, content);
		stmt.setInt(2, senderID);
		stmt.setInt(3, receiverID);
		stmt.setInt(4, postID);
		stmt.setString(5, date);
		stmt.execute();
		stmt.close();
		conn.close();
	}
	
	
	public ArrayList<Post> getMyPost(int userID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "select * from post where postID in (select distinct postID from post where userID="+userID+") and isSolved = false";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		ArrayList<Post> datas = new ArrayList<Post>();
		while(result.next()){
			Post post = new Post();
			System.out.println("Selected postID: "+result.getInt("postID"));
			post.setPostID(result.getInt("postID"));
			post.setTitle(result.getString("title"));
			post.setPostContent(result.getString("postContent"));
			post.setLocation(result.getString("location"));
			post.setDate(result.getString("Date"));
			post.setReward(result.getInt("reward"));
			post.setUserID(result.getInt("userID"));
			post.setName(result.getString("name"));
			post.setSolved(result.getBoolean("isSolved"));
			InputStream is = result.getBinaryStream("attachments");
			System.out.println(is==null);
			
			if(is!=null){

				byte[] imageBytes = IOUtils.toByteArray(is);
	 
	            String base64String = Base64.getEncoder().encodeToString(imageBytes);
	            post.setAttachments(base64String);
	            //System.out.println(base64String);
	            
			}else{
				System.out.println("hehe no image");
				post.setAttachments("no image");
			}
			datas.add(post);
			
		}
		stmt.close();
		conn.close();
		return datas;	
	}
	
	
	public int offer(int messageID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "select * from message where messageID="+messageID;
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet result = stmt.executeQuery();
		result.next();
		int postID = result.getInt("postID");
		int senderID = result.getInt("senderID");
		String content = result.getString("content");
		String date = result.getString("date");
		stmt.close();
		
		sql = "update post set isSolved=true where postID="+postID;
		stmt = conn.prepareStatement(sql);
		stmt.execute();
		stmt.close();
		
	    sql = "insert into solution (postID,solutionContent,userID,date) values(?,?,?,?)";
	    stmt = conn.prepareStatement(sql);
		stmt.setInt(1, postID);
		stmt.setString(2, content);
		stmt.setInt(3, senderID);
		stmt.setString(4, date);
		stmt.execute();
		stmt.close();
		
		conn.close();
		return postID;
	}
	
	public void cancel(int postID) throws Exception{
		conn = new JDBC().getConn();
		String sql = "update post set isSolved=false where postID="+postID;
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.execute();
		stmt.close();
		
		sql = "delete from solution where postID="+postID;
		stmt = conn.prepareStatement(sql);
		stmt.execute();
		stmt.close();
		conn.close();
	}
}
