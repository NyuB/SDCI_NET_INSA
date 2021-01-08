package api.ryu;

public class SetIntFieldAction extends Action {
	private Integer value;

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
