package api.ryu;

public class SetStringFieldAction extends Action {
	private String value;
	public SetStringFieldAction() {
		super();
		this.type = Action.SET_FIELD;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
