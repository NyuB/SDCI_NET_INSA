package api.ryu;
import java.util.LinkedList;
import java.util.List;

public class Action  {
	protected String type = null;
	protected String field = null;

	public static String OUTPUT = "OUTPUT";
	public static String SET_FIELD = "SET_FIELD";
	public static String SET_TP_DST = "SET_TP_DST";

	public static List<Action> DROP() {
		return new LinkedList<>();
	} 

	public static Action Output(int outputPort){
		SetOutputPortAction res = new SetOutputPortAction();
		res.setPort(outputPort);
		return res;
	}

	public static Action SwitchIPDest(String newIp){
		SetStringFieldAction res = new SetStringFieldAction();
		res.type = SET_FIELD;
		res.field = "ipv4_dst";
		res.setValue(newIp);
		return res;
	}

	public static Action SwitchIPSrc(String newIp){
		SetStringFieldAction res = new SetStringFieldAction();
		res.type = SET_FIELD;
		res.field = "ipv4_src";
		res.setValue(newIp);
		return res;
	}

	public static Action SwitchPortDest(int newPort){
		SetIntFieldAction res = new SetIntFieldAction();
		res.setField("tcp_dst");
		res.setValue(newPort);
		return res;
	}

	public Action() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
