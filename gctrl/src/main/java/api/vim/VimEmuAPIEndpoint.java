package api.vim;

import api.RestAPIEndpoint;

public class VimEmuAPIEndpoint extends RestAPIEndpoint {
	public VimEmuAPIEndpoint(String endpoint) {
		super(endpoint);
		this.endpoint += "restapi/";
	}

	public Vnf putRestComputeStart(String datacenter, String image, String vnfName) {
		ComputeStart req = new ComputeStart();
		req.setImage(image);
		String url=endpoint + "compute/" + datacenter + "/" + vnfName;
		System.out.println("URL : "+url);
		return rest.putForObject(url, req, Vnf.class);
	}
}
