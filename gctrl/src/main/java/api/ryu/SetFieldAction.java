package api.ryu;

public class SetFieldAction extends Action {
	protected String field = null;

	public SetFieldAction() {
		super();
		this.type = Action.SET_FIELD;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
