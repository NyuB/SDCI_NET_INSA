package api.middleware;

import api.RestAPIEndpoint;

public class GatewayAPIEndpoint extends RestAPIEndpoint {

	public GatewayAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public PingResponse getRestPing() {
		return rest.getForObject(endpoint + "ping", PingResponse.class);
	}
	public HealthResponse getRestHealth() {
		return rest.getForObject(endpoint + "health", HealthResponse.class);
	}
}