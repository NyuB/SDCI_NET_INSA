package api.vnfconfig;

import api.RestAPIEndpoint;

public class VnfConfigAPIEndpoint extends RestAPIEndpoint {

	public VnfConfigAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public void putRestConfig(VnfConfig config){
		rest.put(endpoint+"config",config);
	}
}
