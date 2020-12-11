package api;
public class RestAPIEndpoint {
	protected JsonRestTemplate rest = new JsonRestTemplate();
	protected String endpoint;

	public RestAPIEndpoint(String endpoint){
		if(!endpoint.endsWith("/")){
			endpoint = endpoint+"/";
		}
		this.endpoint = endpoint;
	}
}
