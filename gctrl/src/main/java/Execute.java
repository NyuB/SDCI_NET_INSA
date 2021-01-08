import api.ryu.RyuAPIEndpoint;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;

import java.util.List;

//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

//
//* Changes the behavior of the managed resource using effectors Changes the behavior of the managed resource using effectors, based on the actions recommended by the plan function.
//*
@SuppressWarnings({"SameParameterValue", "SynchronizeOnNonFinalField"})
class Execute {
	private static List<String> workflow_lists;
	private static final MANOAPI manoapi = new MANOAPI();
	private static final SDNCtrlAPI sdnctlrapi = new SDNCtrlAPI();
	private static final VimEmuAPIEndpoint vim = new VimEmuAPIEndpoint("127.0.0.1", 5001);
	private static final RyuAPIEndpoint ryu = new RyuAPIEndpoint("127.0.0.1", 8080);

	void start() throws InterruptedException {
		Main.logger(this.getClass().getSimpleName(), "Start Execution");
		workflow_lists = Main.shared_knowledge.get_worklow_lists();

		while (Main.run) {
			String current_plan = get_plan();

			// Main.logger(this.getClass().getSimpleName(), "Received Plan : " + current_plan);
			String[] workflow = workflow_generator(current_plan);
			for (int i = 0; i < workflow.length; i++) {
				Main.logger(this.getClass().getSimpleName(), "workflow [" + i + "] : " + workflow[i]);
			}

			for (String w : workflow) {
				Main.logger(this.getClass().getSimpleName(), "UC : " + w);
				String ipWithoutMask;
				Vnf vnf;
				switch (w) {
					case "UC1":
						Main.logger(this.getClass().getSimpleName(), "Nothing to do");
						break;
					case "UC2"://Deploy gw and redirect gfa
						Main.logger(this.getClass().getSimpleName(), "Adding Gateway in DC");
						vnf = manoapi.addGatewayVnf(vim, "10.0.0.1", Knowledge.portS, "DC", "gwUC2");
						ipWithoutMask = vnf.mnIP();
						Main.logger(this.getClass().getSimpleName(), "Redirecting GFA traffic to new GW");
						sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipGI, 8888);
						break;
					case "UC3"://Filter non-gfa traffic
						Main.logger(this.getClass().getSimpleName(), "Adding filter in DC");
						vnf = manoapi.addFilterVnf(vim, Knowledge.ipGFA,Knowledge.ipS, Knowledge.portS,"DC","filterUC3");
						ipWithoutMask = vnf.mnIP();
						Main.logger(this.getClass().getSimpleName(), "Redirecting all gf traffics to vnf");
						sdnctlrapi.vnfInTheMiddle(ryu,Knowledge.switchB, Knowledge.portDCB, Knowledge.portInB, Knowledge.ipGFB, ipWithoutMask, Knowledge.ipGI, 8888);
						sdnctlrapi.vnfInTheMiddle(ryu,Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipGI, 8888);
						break;
					default:
						break;
				}
				Thread.sleep(2000);
				continue;

			}

		}
	}

	//Plan Receiver
	private String get_plan() {
		synchronized (Main.plan.gw_PLAN) {
			try {
				Main.plan.gw_PLAN.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Main.plan.gw_PLAN;
	}

	//Rule-based Workflow Generator
	private String[] workflow_generator(String plan) {
		List<String> plans = Main.shared_knowledge.get_plans();
		for (int i = 0; i < plans.size(); i++) {
			if (plan.contentEquals(plans.get(i))) {
				return workflow_lists.get(i).split("/");
			}
		}
		return null;
        /*
        if (plan.contentEquals(plans.get(0))) {
            return workflow_lists.get(0).split("/");
        } else if (plan.contentEquals(plans.get(1))) {
            return workflow_lists.get(1).split("/");
        } else if (plan.contentEquals(plans.get(2))) {
            return workflow_lists.get(2).split("/");
        } else
            return null;

         */
	}
}
