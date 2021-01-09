package api.vim;

import api.RestAPIEndpoint;

public class VimEmuAPIEndpoint extends RestAPIEndpoint {
	public VimEmuAPIEndpoint(String endpoint) {
		super(endpoint);
		this.endpoint += "restapi/";
	}

	public VimEmuAPIEndpoint(String ip, int port) {
		super(ip, port);
		this.endpoint += "restapi/";
	}

	public Vnf putRestComputeStart(ComputeStart compute, String datacenter, String vnfName) {
		String url = endpoint + "compute/" + datacenter + "/" + vnfName;
		System.out.println("URL : " + url);
		return rest.putForObject(url, compute, Vnf.class);
	}

	public void delRestComputeStop(String datacenter, String vnfName) {
		String url = endpoint + "compute/" + datacenter + "/" + vnfName;
		System.out.println("URL : " + url);
		rest.delete(url);
	}
}
