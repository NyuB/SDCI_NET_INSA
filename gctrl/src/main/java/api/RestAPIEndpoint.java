package api;

public class RestAPIEndpoint {
	protected GsonRestTemplate rest = new GsonRestTemplate();
	protected String endpoint;

	public RestAPIEndpoint(String endpoint) {
		if (!endpoint.endsWith("/")) {
			endpoint = endpoint + "/";
		}
		this.endpoint = endpoint;
	}

	public RestAPIEndpoint(String ip,int port){
		this("http://"+ip+":"+port+"/");
	}
}
