package api.middleware;

import api.RestAPIEndpoint;

public class GatewayAPIEndpoint extends RestAPIEndpoint {

	public GatewayAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public GatewayAPIEndpoint(String ip, int port) {
		super(ip, port);
	}
	protected String constructUrl(String resource){
		return endpoint+resource;
	}
	public PingResponse getRestPing() {
		return rest.getForObject(constructUrl("ping"), PingResponse.class);
	}
	public HealthResponse getRestHealth() {
		return rest.getForObject(constructUrl("health"), HealthResponse.class);
	}
}