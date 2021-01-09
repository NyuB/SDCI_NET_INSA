package api.ryu;

public class SetStringFieldAction extends SetFieldAction {
	protected String value;

	public SetStringFieldAction() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
