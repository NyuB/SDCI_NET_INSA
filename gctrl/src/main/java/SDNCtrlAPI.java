import api.ryu.Action;
import api.ryu.FlowRule;
import api.ryu.Match;
import api.ryu.RyuAPIEndpoint;

import java.util.List;
import java.util.Map;

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

    public void vnfInTheMiddle(RyuAPIEndpoint ryu, int switchId, int outPort, int inPort, String ipHost, String ipVnf, String ipServer){
        //First rule
        Match firstMatch = Match.Ipv4SrcDest(ipHost,ipServer);
        Action a = Action.SwitchIPDest(ipVnf);
        Action b = Action.Output(outPort);
        FlowRule first = new FlowRule();
        first.setDpid(switchId);
        first.setPriority(666);
        first.queueAction(a);
        first.queueAction(b);
        first.setMatch(firstMatch);
        ryu.postRestAddFlowRule(first);

        //Second rule to complete transparency
        Match secondMatch = Match.Ipv4SrcDest(ipVnf,ipHost);
        Action c = Action.SwitchIPSrc(ipServer);
        Action d = Action.Output(inPort);
        FlowRule second = new FlowRule();
        second.setDpid(switchId);
        second.setPriority(666);
        second.queueAction(c);
        second.queueAction(d);
        second.setMatch(secondMatch);
        ryu.postRestAddFlowRule(second);

    }


}
