import api.ryu.FlowRule;
import api.ryu.RyuAPIEndpoint;

import java.io.IOException;
import java.util.Scanner;

public class Manual {
	public static void main(String[] args) throws IOException {
		RyuAPIEndpoint ryu = new RyuAPIEndpoint(args[0]);
		Scanner in = new Scanner(System.in);
		String line;
		System.out.println("Enter command");
		while((line=in.nextLine())!=null && !line.equals("")){
			String[] cmd = line.split(" ");
			for(int i=0;i<cmd.length;i++){
				cmd[i] = cmd[i].toLowerCase();
			}
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
			else{
				System.out.println("Unknown command");
			}
		}
	}
}
