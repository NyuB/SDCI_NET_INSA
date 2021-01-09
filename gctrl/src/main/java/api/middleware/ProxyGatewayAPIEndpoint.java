package api.middleware;

public class ProxyGatewayAPIEndpoint extends GatewayAPIEndpoint {
	String mininetHost;

	public ProxyGatewayAPIEndpoint(String endpoint, String mininetHost) {
		super(endpoint);
		this.mininetHost = mininetHost;
	}

	public ProxyGatewayAPIEndpoint(String ip, int port, String mininetIp, int mininetPort) {
		super(ip, port);
		this.mininetHost = mininetIp + ":" + mininetPort;
	}

	@Override
	protected String constructUrl(String resource) {
		return super.constructUrl(resource) + "?remote=" + mininetHost;
	}
}
