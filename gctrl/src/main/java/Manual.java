import api.middleware.GatewayAPIEndpoint;
import api.middleware.PingResponse;
import api.ryu.Action;
import api.ryu.FlowRule;
import api.ryu.Match;
import api.ryu.RyuAPIEndpoint;
import api.vim.ComputeStart;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Manual {

	public static String askNextLine(Scanner in){
		System.out.println("Enter command");
		return in.nextLine();
	}
	private static Map<String,String> parseOptions(String[] cmd, String token, int index){
		Map<String, String> res = new HashMap<>();
		for(int i = index;i<cmd.length;i++){
			String[] option = cmd[i].split(token);
			res.put(option[0],option[1]);
		}
		return res;
	}
	private static Map<String, String> parseOptions(String[] cmd){
		return parseOptions(cmd,"=",1);
	}
	public static void main(String[] args) throws IOException {
		RyuAPIEndpoint ryu = new RyuAPIEndpoint("http://localhost:8080");
		VimEmuAPIEndpoint vim = new VimEmuAPIEndpoint("http://localhost:5001");
		Scanner in = new Scanner(System.in);
		String line;
		while((line=askNextLine(in))!=null && !line.equals("")){
			String[] cmd = line.split(" ");
			if(cmd.length==0){
				System.out.println("Enter command");
			}
			else if(cmd[0].equals("drop")) {
				int switchId = Integer.parseInt(cmd[1]);
				String ipa = cmd[2];
				String ipb = cmd[3];
				FlowRule flowRule = FlowRule.DropIpv4(switchId,666,ipa,ipb);
				ryu.postRestAddFlowRule(flowRule);
			}
			else if(cmd[0].equals("redirect")){//switch ipsource ipdest
				int switchId = Integer.parseInt(cmd[1]);
				String ipFrom = cmd[2];
				String ipDest = cmd[3];
				int portDest = Integer.parseInt(cmd[4]); 
				FlowRule flowRule = FlowRule.RedirectIpv4(switchId, 666, ipFrom, ipDest, portDest);
				ryu.postRestAddFlowRule(flowRule);

			}
			else if(cmd[0].equals("vnf")){//datacenter image vnf [dockercommands,...]
				ComputeStart computeStart = new ComputeStart();
				computeStart.setImage(cmd[2]);
				if(cmd.length>4){
					String dcmd = "";
					for(int i=4;i<cmd.length;i++){
						if(i!=4)dcmd+=" ";
						dcmd+=cmd[i];
					}
					computeStart.setDocker_command(dcmd);
				}
				Vnf vnf = vim.putRestComputeStart(computeStart,cmd[1],cmd[3]);
				System.out.println(vnf);
			}
			else if(cmd[0].equals("mim")){//mim dpid=3 out_port=3 in_port=1 src=10.0.0.3 dst=10.0.0.1 mid=10.0.0.12 tsp=true del=false
				Map<String, String> options = parseOptions(cmd);
				int dpid = Integer.parseInt(options.get("dpid"));
				String ipFrom = options.get("src");
				String ipTo = options.get("dst");
				String ipMid = options.get("mid");
				boolean transparent = Boolean.parseBoolean(options.get("tsp"));
				boolean delete = Boolean.parseBoolean(options.get("del"));
				int outputPort = Integer.parseInt(options.get("out_port"));
				int inputPort = Integer.parseInt(options.get("in_port"));
				//First rule
				Match firstMatch = Match.Ipv4SrcDest(ipFrom,ipTo);
				Action a = Action.SwitchIPDest(ipMid);
				Action b = Action.Output(outputPort);
				FlowRule first = new FlowRule();
				first.setDpid(dpid);
				first.setPriority(666);
				first.queueAction(a);
				first.queueAction(b);
				first.setMatch(firstMatch);
				if(delete){
					ryu.postRestDeleteFlowRule(first);
				}
				else {
					ryu.postRestAddFlowRule(first);
				}
				//Second rule to complete transparency
				Match secondMatch = Match.Ipv4SrcDest(ipMid,ipFrom);
				Action c = Action.SwitchIPSrc(ipTo);
				Action d = Action.Output(inputPort);
				FlowRule second = new FlowRule();
				second.setDpid(dpid);
				second.setPriority(666);
				second.queueAction(c);
				second.queueAction(d);
				second.setMatch(secondMatch);
				if(delete && transparent){
					ryu.postRestDeleteFlowRule(second);
				}
				else if(transparent) {
					ryu.postRestAddFlowRule(second);
				}
			}
			else if (cmd[0].equals("ping")){
				String endpoint = "http://"+cmd[1];
				GatewayAPIEndpoint test = new GatewayAPIEndpoint(args[0]);
				PingResponse pong = test.getRestPing();
				System.out.println("Received pong value : " + pong.getPong());
			}
			else{
				System.out.println("Unknown command");
			}
		}
	}
}
