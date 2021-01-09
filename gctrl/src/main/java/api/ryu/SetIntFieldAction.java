package api.ryu;

public class SetIntFieldAction extends SetFieldAction {
	protected Integer value;

	public SetIntFieldAction() {
		super();
		this.type = Action.SET_FIELD;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
