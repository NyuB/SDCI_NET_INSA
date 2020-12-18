package api.vim;

public class Network {
	private String dc_portname;
	private String intf_name;
	private String ip;
	private String mac;
	private String status;
	private boolean up;

	@Override
	public String toString() {
		return "Network{" +
				"dc_portname='" + dc_portname + '\'' +
				", intf_name='" + intf_name + '\'' +
				", ip='" + ip + '\'' +
				", mac='" + mac + '\'' +
				", status='" + status + '\'' +
				", up=" + up +
				'}';
	}

	public Network() {
	}

	public String getDc_portname() {
		return dc_portname;
	}

	public void setDc_portname(String dc_portname) {
		this.dc_portname = dc_portname;
	}

	public String getIntf_name() {
		return intf_name;
	}

	public void setIntf_name(String intf_name) {
		this.intf_name = intf_name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
}
