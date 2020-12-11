package api.ryu;

import api.RestAPIEndpoint;

public class RyuAPIEndpoint extends RestAPIEndpoint {
	public RyuAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public void postRestAddFlowRule(FlowRule flowRule){
		Object ok = rest.postForObject(endpoint+"stats/flowentry/add",flowRule,Object.class);
	}
}
