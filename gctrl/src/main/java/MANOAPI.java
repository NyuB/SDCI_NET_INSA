import api.vim.ComputeStart;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;
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

	private static int PORT_DFLT = 8888;
	private static int RETRIES_DFLT = 5;
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

	public Vnf addLoadBalancingVnf(VimEmuAPIEndpoint vim, String ipA, int portA, String ipB, int portB, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_LB);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		VnfConfig config = VnfConfig.ABConfig(ipA, portA, ipB, portB);
		String allocatedIp = vnf.getNetwork().get(0).getIp();
		String cheatIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp + ":" + PORT_DFLT);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(cheatIp, PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
		return vnf;
	}

	public Vnf addGatewayVnf(VimEmuAPIEndpoint vim, String remoteIp, int remotePort, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_GW);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		String ipWithoutMask = vnf.mnIP();
		String cheatIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + ipWithoutMask + ":" + PORT_DFLT);
		VnfConfig config = VnfConfig.LocalRemoteConfig(ipWithoutMask, remoteIp, remotePort);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(cheatIp, PORT_DFLT);
		System.out.println("Sending configuration request to vnf");
		vnfConfigAPIEndpoint.putRestConfig(config, RETRIES_DFLT, RETRY_PERIOD_DFLT);
		System.out.println("Sent configuration to vnf");
		return vnf;
	}

	public Vnf addFilterVnf(VimEmuAPIEndpoint vim, String vip, String remoteIp, int remotePort, String dc, String name) {
		ComputeStart computeStart = new ComputeStart();
		computeStart.setImage(Knowledge.IMG_FILTER);
		System.out.println("Sending vnf creation request " + computeStart.getImage());
		Vnf vnf = vim.putRestComputeStart(computeStart, dc, name);
		String allocatedIp = vnf.getNetwork().get(0).getIp();
		String cheatIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp + ":" + PORT_DFLT);
		VnfConfig config = VnfConfig.VipRemoteConfig(vip, remoteIp, remotePort);
		VnfConfigAPIEndpoint vnfConfigAPIEndpoint = new VnfConfigAPIEndpoint(cheatIp, PORT_DFLT);
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
		String cheatIp = vnf.getDocker_network();
		System.out.println("VNF created, IP allocated : " + allocatedIp);
		System.out.println("Docker @ddr : " + cheatIp + ":" + PORT_DFLT);
		return vnf;
	}
}
