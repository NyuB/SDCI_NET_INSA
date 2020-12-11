import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader ;
import java.net.HttpURLConnection ;
import java.net.URL ;

import org.springframework.web.client.RestTemplate;

class PongModel {

    private int pong ;

    public PongModel() {}

    public void setPong(int pong) {
        this.pong = pong ;
    }    

    public int getPong() {
        return this.pong ;
    } 
}

public class Sensor {

    private String url ;

    public Sensor(String url) {
        this.url = url ;
    }

    public static void main(String[] args) throws IOException {
		sendGetREST();
		System.out.println("GET DONE");
	}

    public static void sendGetREST() {
        RestTemplate rt = new RestTemplate() ;
        String geturl = "http://127.0.0.1:8181/ping" ;
        PongModel pm = rt.getForObject(geturl, PongModel.class) ;
        System.out.print(pm.getPong()) ;
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