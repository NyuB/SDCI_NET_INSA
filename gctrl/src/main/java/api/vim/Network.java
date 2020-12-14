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
}
