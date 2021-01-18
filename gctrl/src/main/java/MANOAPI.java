import api.vim.ComputeStart;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;
import api.EndpointInfo;
import api.vnfconfig.VnfConfig;
import api.vnfconfig.VnfConfigAPIEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author couedrao on 27/11/2019.
 * @project gctrl
 */
class MANOAPI {

	private static int VNF_PORT_DFLT = 8888;
	private static int RETRIES_DFLT = 10;
	private static int RETRY_PERIOD_DFLT = 750;

	String deploy_gw(Map<String, String> vnfinfos) {
		String ip = "192.168.0." + (new Random().nextInt(253) + 1);
		Main.logger(this.getClass().getSimpleName(), "Deploying VNF ...");

		//printing
		for (Entry<String, String> e : vnfinfos.entrySet()) {
			Main.logger(this.getClass().getSimpleName(), "\t" + e.getKey() + " : " + e.getValue());
		}
		//TODO

		return ip;
	}

	List<String> deploy_multi_gws_and_lb(List<Map<String, String>> vnfsinfos) {
		List<String> ips = new ArrayList<>();
		//TODO

		for (Map<String, String> vnfsinfo : vnfsinfos) {
			ips.add(deploy_gw(vnfsinfo));
		}

		return ips;
	}

	public Vnf addDualLoadBalancerVnf(VimEmuAPIEndpoint vim, String ipA, int portA, String ipB, int portB, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_LB);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		VnfConfig config = VnfConfig.ABConfig(ipA, portA, ipB, portB);
		String allocatedIp = vnf.getNetwork().get(0).getIp();
		String dockerIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp + ":" + VNF_PORT_DFLT);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(dockerIp, VNF_PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
		return vnf;
	}

	public Vnf addMultiLoadBalancerVnf(VimEmuAPIEndpoint vim, String dc, String name){
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_MLB);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf mlb = vim.putRestComputeStart(computeStart, dc, name);
		String allocatedIp = mlb.getNetwork().get(0).getIp();
		System.out.println("VNF created, IP allocated : " + allocatedIp + ":" + VNF_PORT_DFLT);
		return mlb;
	}

	public void configMultiLoadBalancer(Vnf mlb, List<EndpointInfo> endpoints){
		VnfConfig config = VnfConfig.EndpointsListConfig(endpoints);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(mlb.getDocker_network(), VNF_PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
	}


	/**
	 * Deploy a new applicative gateway and configure it toward a given server
	 * @param vim VIM-EMU REST endpoint to execute deployment operations
	 * @param remoteIp server Ip
	 * @param remotePort server port
	 * @param dc Datacenter name
	 * @param name to give to the gateway
	 * @return
	 */
	public Vnf addGatewayVnf(VimEmuAPIEndpoint vim, String remoteIp, int remotePort, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_GW);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);//Instanciate VNF via REST request

		String ipWithoutMask = vnf.mnIP();
		String dockerIp = vnf.getDocker_network();//Use docker ip for GC->vnf communication
		System.out.println("VNF created, IP allocated : " + ipWithoutMask + ":" + VNF_PORT_DFLT);
		VnfConfig config = VnfConfig.LocalRemoteConfig(ipWithoutMask, remoteIp, remotePort);
		config.setLocal_name(name);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(dockerIp, VNF_PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
		return vnf;
	}

	public Vnf addProxyVnf(VimEmuAPIEndpoint vim, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_PROXY);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		String allocatedIp = vnf.getNetwork().get(0).getIp();
		String dockerIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp);
		System.out.println("Docker @ddr : " + dockerIp + ":" + VNF_PORT_DFLT);
		return vnf;
	}

	public Vnf addFilterVnf(VimEmuAPIEndpoint vim, String vip, String remoteIp, int remotePort, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_FILTER);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		String allocatedIp = vnf.getNetwork().get(0).getIp();
		String dockerIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp + ":" + VNF_PORT_DFLT);
		VnfConfig config = VnfConfig.VipRemoteConfig(vip, remoteIp, remotePort);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(dockerIp, VNF_PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
		return vnf;
	}

	public void removeVnf(VimEmuAPIEndpoint vim, String dc, String vnfName){
		System.out.println("Removing vnf "+vnfName+" from datacenter "+dc);
		vim.delRestComputeStop(dc,vnfName);
	}
}
