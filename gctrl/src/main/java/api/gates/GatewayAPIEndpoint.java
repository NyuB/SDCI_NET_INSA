package api.gates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader ;
import java.net.HttpURLConnection ;
import java.net.URL ;

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


    public static void sendGet(String info) throws IOException {
        URL urlGet = new URL("http://127.0.0.1:8181"+info) ;
        HttpURLConnection con = (HttpURLConnection) urlGet.openConnection() ;
        con.setRequestMethod("GET") ;

        int responseCode = con.getResponseCode() ;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

            // print result
			System.out.println(response.toString());
		} 
        else {
			System.out.println("GET request not worked");
        }
    }
}