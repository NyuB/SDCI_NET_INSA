package api.ryu;

import java.util.LinkedList;
import java.util.List;

public class FlowRule {
	private int priority;
	private int table_id =0;
	private int idle_timeout = 30;
	private int hard_timeout=30;
	private int flags = 0;
	private int cookie=0;
	private int cookie_mask = 0;
	private List<Action> actions = new LinkedList<>();
	private Match match = new Match();
}
