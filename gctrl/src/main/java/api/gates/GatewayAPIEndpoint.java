package api.gates;

import java.io.IOException;
import api.RestAPIEndpoint;

public class GatewayAPIEndpoint extends RestAPIEndpoint {

	public GatewayAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Testing class : "+ GatewayAPIEndpoint.class.getName());
		if(args.length==0){
			System.out.println("Provide api endpoint as command line argument");
		}
		else{
			GatewayAPIEndpoint test = new GatewayAPIEndpoint(args[0]);
			PingResponse pong = test.getRestPing();
			System.out.println("Received pong value : "+pong.getPong());
			System.out.println("GET DONE");
		}
	}

	public PingResponse getRestPing(){
    	return rest.getForObject(endpoint+"ping",PingResponse.class);
	}
}