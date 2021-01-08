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


	//TODO => move hardcoded switch params to Knowledge
	/*
	private static int switchA = 3;
	private static int switchB = 4;
	private static int portInA = 1;
	private static int portInB = 1;
	private static int portDCA = 3;
	private static int portDCB = 3;
	private static String ipGFA = "10.0.0.3";
	private static String ipS = "10.0.0.1";
	*/

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
				switch (w) {
					case "UC1":
						Main.logger(this.getClass().getSimpleName(), "Nothing to do");
						break;
					case "UC2":
						Main.logger(this.getClass().getSimpleName(), "Deploying GW");
						String newdestip = manoapi.deploy_gw(Main.shared_knowledge.getGwinfo());
						Main.shared_knowledge.setNewdestip(newdestip);
						Main.shared_knowledge.setOldgwip(newdestip);
						break;
					case "UC3":
						Main.logger(this.getClass().getSimpleName(), "Redirecting Traffic");
						String status = sdnctlrapi.redirect_traffic(Main.shared_knowledge.getOlddestip(), Main.shared_knowledge.getNewdestip());
						Main.logger(this.getClass().getSimpleName(), status);
						break;
					case "UC4":
						Main.logger(this.getClass().getSimpleName(), "Deploying LB+GWs");
						List<String> newgwsip = manoapi.deploy_multi_gws_and_lb(Main.shared_knowledge.getGwsinfo());
						Main.shared_knowledge.setLbip(newgwsip.get(0));
						Main.shared_knowledge.setNewgwsip(newgwsip.subList(1, newgwsip.size()));
						break;
					case "UC5":
						Main.logger(this.getClass().getSimpleName(), "Inserting a loadbalancer");
						status = sdnctlrapi.insert_a_loadbalancer(Main.shared_knowledge.getOldgwip(), Main.shared_knowledge.getLbip(), Main.shared_knowledge.getNewgwsip());
						Main.logger(this.getClass().getSimpleName(), status);
						break;
					case "UC6":
						Main.logger(this.getClass().getSimpleName(), "Removing less important traffic");
						status = sdnctlrapi.remove_less_important_traffic(Main.shared_knowledge.getImportantsrcip());
						Main.logger(this.getClass().getSimpleName(), status);
						break;
					case "UC7"://Test use case, deploy gw and redirect gfa
						Main.logger(this.getClass().getSimpleName(), "Adding Gateway in DC");
						Vnf vnf = manoapi.addGatewayVnf(vim, "10.0.0.1", 8080, "DC", "gwUC7");
						String ipWithoutMask = vnf.mnIP();
						Main.logger(this.getClass().getSimpleName(), "Redirecting GFA traffic to new GW");
						sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipS);
					default:
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
