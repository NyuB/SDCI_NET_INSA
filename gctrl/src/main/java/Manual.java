import api.ryu.FlowRule;
import api.ryu.RyuAPIEndpoint;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;

import java.io.IOException;
import java.util.Scanner;

public class Manual {
	public static void main(String[] args) throws IOException {
		RyuAPIEndpoint ryu = new RyuAPIEndpoint("http://localhost:8080");
		VimEmuAPIEndpoint vim = new VimEmuAPIEndpoint("http://localhost:5001");
		Scanner in = new Scanner(System.in);
		String line;
		System.out.println("Enter command");
		while((line=in.nextLine())!=null && !line.equals("")){
			String[] cmd = line.split(" ");
			if(cmd.length==0){
				System.out.println("Enter command");
				continue;
			}
			else if(cmd[0].equals("drop")) {
				int switchId = Integer.parseInt(cmd[1]);
				String ipa = cmd[2];
				String ipb = cmd[3];
				FlowRule flowRule = FlowRule.DropIpv4(switchId,666,ipa,ipb);
				ryu.postRestAddFlowRule(flowRule);
			}
			else if(cmd[0].equals("redirect")){
				int switchId = Integer.parseInt(cmd[1]);
				String ipFrom = cmd[2];
				String ipDest = cmd[3];
				int portDest = Integer.parseInt(cmd[4]); 
				FlowRule flowRule = FlowRule.RedirectIpv4(switchId, 666, ipFrom, ipDest, portDest);
				ryu.postRestAddFlowRule(flowRule);

			}
			else if(cmd[0].equals("vnf")){
				Vnf vnf = vim.putRestComputeStart(cmd[1], cmd[2], cmd[3]);
				System.out.println(vnf);
			}
			else{
				System.out.println("Unknown command");
			}
		}
	}
}
