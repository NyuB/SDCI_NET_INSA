package api.vnfconfig;

import api.RestAPIEndpoint;

public class VnfConfigAPIEndpoint extends RestAPIEndpoint {

	public VnfConfigAPIEndpoint(String endpoint) {
		super(endpoint);
	}

	public void putRestConfig(VnfConfig config){
		rest.put(endpoint+"config",config);
	}
	public void putRestConfig(VnfConfig config, int retries, int period){
		for(int i = 0;i<retries;i++){
			try{
				putRestConfig(config);
				return;
			}
			catch (Exception e){
				System.out.println("Retrying config request");
				try {
					Thread.sleep(period);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		putRestConfig(config);

	}

	public VnfConfigAPIEndpoint(String ip, int port) {
		super(ip, port);
	}
}
