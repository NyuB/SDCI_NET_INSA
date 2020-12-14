package api.ryu;
import java.util.LinkedList;
import java.util.List;

public class Action  {
	private String type = null;
	private Integer port = null;
	private String field = null;
	private String value = null;

	public static String OUTPUT = "OUTPUT";
	public static String SET_FIELD = "SET_FIELD";

	public static List<Action> DROP() {
		return new LinkedList<>();
	} 

	public static Action Output(int outputPort){
		Action res = new Action();
		res.type = OUTPUT;
		res.port = outputPort;
		return res;
	}

	public static Action SwitchIPDest(String newIp){
		Action res = new Action();
		res.type = SET_FIELD;
		res.field = "ipv4_dst";
		res.value = newIp;
		return res;
	}

	public static Action SwitchIPSrc(String newIp){
		Action res = new Action();
		res.type = SET_FIELD;
		res.field = "ipv4_src";
		res.value = newIp;
		return res;
	}

	public static Action SetField(String field, String value){
		Action res = new Action();
		res.type = SET_FIELD;
		res.field = field;
		res.value = value;

	}

	public Action() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
