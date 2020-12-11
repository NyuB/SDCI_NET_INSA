package api.ryu;
import java.util.LinkedList;
import java.util.List;

public class Action  {
	private String type = null;
	private Integer port = null;

	public static List<Action> DROP = new LinkedList<>();

	public static Action Output(int outputPort){
		Action res = new Action();
		res.type = "OUTPUT";
		res.port = outputPort;
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

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
