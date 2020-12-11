package api;
public class RestAPIEndpoint {
	protected JsonRestTemplate rest = new JsonRestTemplate();
	protected String endpoint;

	public RestAPIEndpoint(String endpoint){
		this.endpoint = endpoint;
	}
}
