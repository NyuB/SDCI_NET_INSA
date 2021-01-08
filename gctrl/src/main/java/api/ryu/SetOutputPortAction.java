package api.ryu;

public class SetOutputPortAction extends Action {
	private Integer port = null;

	public SetOutputPortAction() {
		super();
		this.type = Action.OUTPUT;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
