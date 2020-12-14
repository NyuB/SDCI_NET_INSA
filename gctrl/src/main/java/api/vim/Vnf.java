package api.vim;

import java.util.List;

public class Vnf {
	private String datacenter;
	private String name;
	private String image;
	private List<Network> network;

	public String getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Network> getNetwork() {
		return network;
	}

	public void setNetwork(List<Network> network) {
		this.network = network;
	}

	@Override
	public String toString() {
		String res= "Vnf{" +
				"datacenter='" + datacenter + '\'' +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				'}';
		for(Network n : network){
			res+=n+", ";
		}
		return res;
	}
}
