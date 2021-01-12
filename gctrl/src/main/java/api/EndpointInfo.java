package api;

public class EndpointInfo {
	private String ip = null;
	private Integer port = null;

	public EndpointInfo() {
	}

	public EndpointInfo(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
