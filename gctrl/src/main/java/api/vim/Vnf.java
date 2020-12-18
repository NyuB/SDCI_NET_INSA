package api.vim;

import java.util.List;

public class Vnf {
	private String datacenter;
	private String name;
	private String image;
	private List<Network> network;
	private String docker_network;

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

	public String getDocker_network(){
		return this.docker_network;
	}

	public void setDocker_network(String docker_network){
		this.docker_network = docker_network;
	}

	@Override
	public String toString() {
		String res= "Vnf{" +
				"datacenter='" + datacenter + '\'' +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", docker_network='" + docker_network + '\'' +
				'}';
		for(Network n : network){
			res+=n+", ";
		}
		return res;
	}
}
