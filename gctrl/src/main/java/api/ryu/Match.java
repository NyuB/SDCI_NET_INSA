package api.ryu;

import java.util.HashMap;

public class Match {
	//Null fields will not be serialized by Gson
	private String ipv4_src = null;
	private String ipv4_dst = null;
	private String eth_type = null;
	private Integer in_port = null;

	public Match(){

	}

	public String getIpv4_src() {
		return ipv4_src;
	}

	public void setIpv4_src(String ipv4_src) {
		this.ipv4_src = ipv4_src;
	}

	public String getIpv4_dst() {
		return ipv4_dst;
	}

	public void setIpv4_dst(String ipv4_dst) {
		this.ipv4_dst = ipv4_dst;
	}

	public String getEth_type() {
		return eth_type;
	}

	public void setEth_type(String eth_type) {
		this.eth_type = eth_type;
	}

	public Integer getIn_port() {
		return in_port;
	}

	public void setIn_port(Integer in_port) {
		this.in_port = in_port;
	}
}
