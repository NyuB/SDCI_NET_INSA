package api.vim;

public class Datacenter {
	private String label = null;
	private String internalname = null;
	private Object metadata = null;
	private int n_running_containers = 0;
	private String Switch = null;

	public Datacenter() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInternalname() {
		return internalname;
	}

	public void setInternalname(String internalname) {
		this.internalname = internalname;
	}

	public Object getMetadata() {
		return metadata;
	}

	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public int getN_running_containers() {
		return n_running_containers;
	}

	public void setN_running_containers(int n_running_containers) {
		this.n_running_containers = n_running_containers;
	}

	public String getSwitch() {
		return Switch;
	}

	public void setSwitch(String aSwitch) {
		Switch = aSwitch;
	}
}
