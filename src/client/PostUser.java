package client;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PostUser {

	public String awsAddress = "http://ec2-54-191-107-141.us-west-2.compute.amazonaws.com/";
	public void createUser(String name) {
		try {
			Client client = Client.create();

			WebResource webResource = client.resource(awsAddress + ":8080/DoMeAFavor_Server/user/createUser?");
			MultivaluedMap<String, String> postBody = new MultivaluedMapImpl();
			postBody.add("name", name);
			System.out.println("bb");

			ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class,
					postBody);

			if (response.getStatus() != 200 && response.getEntity(String.class) == "Success") {
				throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());

			}

			System.out.println("User created Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
