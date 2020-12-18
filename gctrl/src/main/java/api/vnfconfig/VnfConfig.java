package api.vnfconfig;

import java.util.Map;

public class VnfConfig {
	private String ip_A;
	private String ip_B;
	private String local_ip;
	private String local_name;
	private String remote_ip;
	private String remote_port;
	private String remote_name;

	public VnfConfig() {
	}
	public VnfConfig(Map<String,String> map){
		this.setIp_A(map.get("ip_A"));
		this.setIp_B(map.get("ip_B"));
		this.setLocal_ip(map.get("local_ip"));
		this.setLocal_name(map.get("local_name"));
		this.setRemote_ip(map.get("remote_ip"));
		this.setRemote_port(map.get("remote_port"));
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

	public String getRemote_port() {
		return remote_port;
	}

	public void setRemote_port(String remote_port) {
		this.remote_port = remote_port;
	}

	public String getRemote_name() {
		return remote_name;
	}

	public void setRemote_name(String remote_name) {
		this.remote_name = remote_name;
	}
}
