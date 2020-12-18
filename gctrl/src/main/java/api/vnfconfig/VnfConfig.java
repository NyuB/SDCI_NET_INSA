package api.vnfconfig;

import java.util.Map;

public class VnfConfig {
	private String ip_A;
	private String ip_B;
	private Integer port_A = null;
	private Integer port_B = null;
	private String local_ip;
	private String local_name;
	private String remote_ip;
	private Integer remote_port = null;
	private String remote_name;

	public static VnfConfig ABConfig(String ip_A, int port_A, String ip_B, int port_B){
		VnfConfig res = new VnfConfig();
		res.setIp_A(ip_A);
		res.setIp_B(ip_B);
		res.setPort_A(port_A);
		res.setPort_B(port_B);
		return res;
	}
	public static VnfConfig LocalRemoteConfig(String localIp, String remoteIp,int remotePort){
		VnfConfig res = new VnfConfig();
		res.setLocal_ip(localIp);
		res.setRemote_ip(remoteIp);
		res.setRemote_port(remotePort);
		return res;
	}

	public VnfConfig() {
	}
	public VnfConfig(Map<String,String> map){
		this.setIp_A(map.get("ip_A"));
		this.setIp_B(map.get("ip_B"));
		String pA = map.get("port_A");
		String pB = map.get("port_B");
		if(pA!=null)this.setPort_A(Integer.parseInt(pA));
		if(pB!=null)this.setPort_B(Integer.parseInt(pB));
		this.setLocal_ip(map.get("local_ip"));
		this.setLocal_name(map.get("local_name"));
		this.setRemote_ip(map.get("remote_ip"));
		String pR = map.get("remote_port");
		if(pR!=null)this.setRemote_port(Integer.parseInt(pR));
		this.setRemote_name(map.get("remote_name"));
	}

	public String getIp_A() {
		return ip_A;
	}

	public void setIp_A(String ip_A) {
		this.ip_A = ip_A;
	}

	public String getIp_B() {
		return ip_B;
	}

	public void setIp_B(String ip_B) {
		this.ip_B = ip_B;
	}

	public Integer getPort_A() {
		return port_A;
	}

	public void setPort_A(Integer port_A) {
		this.port_A = port_A;
	}

	public Integer getPort_B() {
		return port_B;
	}

	public void setPort_B(Integer port_B) {
		this.port_B = port_B;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}

	public String getLocal_name() {
		return local_name;
	}

	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}

	public String getRemote_ip() {
		return remote_ip;
	}

	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
	}

	public Integer getRemote_port() {
		return remote_port;
	}

	public void setRemote_port(Integer remote_port) {
		this.remote_port = remote_port;
	}

	public String getRemote_name() {
		return remote_name;
	}

	public void setRemote_name(String remote_name) {
		this.remote_name = remote_name;
	}
}
