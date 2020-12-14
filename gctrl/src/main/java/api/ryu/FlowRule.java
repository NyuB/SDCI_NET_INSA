package api.ryu;

import java.util.LinkedList;
import java.util.List;

public class FlowRule {
	private int dpid;
	private int priority=0;
	private int table_id = 0;
	private int idle_timeout = 30;
	private int hard_timeout=30;
	private Integer flags = null;
	private Integer cookie=null;
	private Integer cookie_mask = null;
	private List<Action> actions = Action.DROP();
	private Match match = new Match();

	public static FlowRule DropIpv4(int switchId, int priority,String src,String dest){
		FlowRule res = new FlowRule();
		res.dpid = switchId;
		res.priority = priority;
		Match match = Match.Ipv4SrcDest(src,dest);
		res.match = match;
		return res;
	}

	public static FlowRule RedirectIpv4(int switchId, int priority, String src, String newDest, int newPort){
		FlowRule res = new FlowRule();
		res.dpid = switchId;
		res.priority = priority;
		Match match = Match.Ipv4Src(src);
		res.match = match;
		res.actions.add(Action.SwitchIPDest(newDest));
		res.actions.add(Action.Output(newPort));
		return res;
	}

	public FlowRule(){

	}

	public int getDpid() {
		return dpid;
	}

	public void setDpid(int dpid) {
		this.dpid = dpid;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getTable_id() {
		return table_id;
	}

	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}

	public int getIdle_timeout() {
		return idle_timeout;
	}

	public void setIdle_timeout(int idle_timeout) {
		this.idle_timeout = idle_timeout;
	}

	public int getHard_timeout() {
		return hard_timeout;
	}

	public void setHard_timeout(int hard_timeout) {
		this.hard_timeout = hard_timeout;
	}

	public Integer getFlags() {
		return flags;
	}

	public void setFlags(Integer flags) {
		this.flags = flags;
	}

	public Integer getCookie() {
		return cookie;
	}

	public void setCookie(Integer cookie) {
		this.cookie = cookie;
	}

	public Integer getCookie_mask() {
		return cookie_mask;
	}

	public void setCookie_mask(Integer cookie_mask) {
		this.cookie_mask = cookie_mask;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}
}
