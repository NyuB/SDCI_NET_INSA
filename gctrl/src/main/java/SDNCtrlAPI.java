import api.ryu.Action;
import api.ryu.FlowRule;
import api.ryu.Match;
import api.ryu.RyuAPIEndpoint;

import java.util.Arrays;
import java.util.List;

/**
 * @author couedrao on 27/11/2019.
 * @project gctrl
 */
class SDNCtrlAPI {

	String redirect_traffic(String olddestip, String newdestip) {
		String status = "OK";
		Main.logger(this.getClass().getSimpleName(), "olddestip = " + olddestip + "; newdestip = " + newdestip);
		//TODO

		return status;
	}

	String insert_a_loadbalancer(String oldgwip, String lbip, List<String> newgwsip) {
		String status = "OK";
		Main.logger(this.getClass().getSimpleName(), "oldgwip = " + oldgwip + "; lbip = " + lbip + "; newgwsip = " + newgwsip);
		//TODO

		return status;
	}

	String remove_less_important_traffic(String importantsrcip) {
		String status = "OK";
		Main.logger(this.getClass().getSimpleName(), "importantsrcip = " + importantsrcip);
		//TODO

		return status;
	}

	public List<FlowRule> vnfInTheMiddle(RyuAPIEndpoint ryu, int switchId, int switchToMidPort, int switchToSrcPort, String ipSrc, String ipMid, String ipDest, int portMid, int portDest) {
		//First rule : redirect request from src to server
		Match firstMatch = Match.Ipv4SrcDest(ipSrc, ipDest);
		Action a = Action.SwitchIPDest(ipMid);
		//There are currently problems with ryu/opflw port-modification requests, uncomment this when solved
		//Action b = Action.SwitchPortDest(portMid);
		Action c = Action.Output(switchToMidPort);
		FlowRule first = new FlowRule();
		first.setDpid(switchId);
		first.setPriority(666);
		first.queueAction(a);
		//first.queueAction(b);
		first.queueAction(c);
		first.setMatch(firstMatch);
		ryu.postRestAddFlowRule(first);

		//Second rule to complete transparency : replace source ip and port in response from middle
		Match secondMatch = Match.Ipv4SrcDest(ipMid, ipSrc);
		Action d = Action.SwitchIPSrc(ipDest);
		//There are currently problems with ryu/opflw port-modification requests, uncomment this when solved
		//Action e = Action.SwitchPortSrc(portDest);
		Action f = Action.Output(switchToSrcPort);
		FlowRule second = new FlowRule();
		second.setDpid(switchId);
		second.setPriority(666);
		second.queueAction(d);
		//second.queueAction(e);
		second.queueAction(f);
		second.setMatch(secondMatch);
		ryu.postRestAddFlowRule(second);
		return Arrays.asList(first, second);

	}

	public void removeRule(RyuAPIEndpoint ryu, FlowRule rule){
		System.out.println("Removing flow rule");
		ryu.postRestDeleteFlowRule(rule);
	}


}
