package client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GetUser {

	public String awsAddress = "http://ec2-54-191-107-141.us-west-2.compute.amazonaws.com";

	public void getUser(String user) {
		try {
			Client client = Client.create();

//			WebResource webResource = client.resource("http://localhost:8080/DoMeAFavor_Server/REST/user/getUser?");
			WebResource webResource = client.resource(awsAddress + ":8080/DoMeAFavorServer/rest/user/getUser");

//			ClientResponse response = webResource.queryParam("name", user).get(ClientResponse.class);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());

			}

			String output = response.getEntity(String.class);

			System.out.println(output);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
