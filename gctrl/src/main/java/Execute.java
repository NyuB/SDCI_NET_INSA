import api.EndpointInfo;
import api.ryu.FlowRule;
import api.ryu.RyuAPIEndpoint;
import api.vim.VimEmuAPIEndpoint;
import api.vim.Vnf;

import java.util.ArrayList;
import java.util.Collections;
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
	private static Vnf dedicatedGW = null;
	private static Vnf filter = null;
	private static Vnf loadBalancer = null;
	private static List<FlowRule> activeRules = new ArrayList<>();
	private static List<Vnf> additionalGWs = new ArrayList<>();

	private static long lastActionTimestamp = 0L;
	private static long minimalDelayBeforeReset = 30000L;

	public static synchronized void setMinimalDelayBeforeReset(long delay){
		minimalDelayBeforeReset = delay;
	}

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
				switch (w) {
					case "UC1":
						Main.logger(this.getClass().getSimpleName(), "Nothing to do");
						break;
					case "UC2"://Deploy gw dedicated to gfa
						lastActionTimestamp = System.currentTimeMillis();
						Main.logger(this.getClass().getSimpleName(), "Adding Dedicated Gateway in DC");
						dedicatedGW = manoapi.addGatewayVnf(vim, Knowledge.ipS, Knowledge.portS, "DC", "gwUC2");
						ipWithoutMask = dedicatedGW.mnIP();
						Main.logger(this.getClass().getSimpleName(), "Redirecting GFA traffic to new GW");
						activeRules.addAll(sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipGI, 8888, 8888));
						break;
					case "UC3"://Filter non-gfa traffic
						lastActionTimestamp = System.currentTimeMillis();
						Main.logger(this.getClass().getSimpleName(), "Adding filter in DC");
						filter = manoapi.addFilterVnf(vim, Knowledge.ipGFA, Knowledge.ipGI, Knowledge.portGI, "DC", "filterUC3");
						ipWithoutMask = filter.mnIP();
						Main.logger(this.getClass().getSimpleName(), "Redirecting all gf traffics to vnf");
						activeRules.addAll(sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchB, Knowledge.portDCB, Knowledge.portInB, Knowledge.ipGFB, ipWithoutMask, Knowledge.ipGI, 8888, 8888));
						//activeRules.addAll(sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipGI, 8888, 8888));
						break;
					case "UC4"://Add a gateway to load balancing pool
						lastActionTimestamp = System.currentTimeMillis();
						EndpointInfo gi = new EndpointInfo(Knowledge.ipGI,Knowledge.portGI);
						if(loadBalancer == null){
							Main.logger(this.getClass().getSimpleName(), "Adding multi-load-balancer in DC");
							loadBalancer = manoapi.addMultiLoadBalancerVnf(vim, "DC","mlbUC4");
							ipWithoutMask = loadBalancer.mnIP();
							List<EndpointInfo> init = Collections.singletonList(gi);
							manoapi.configMultiLoadBalancer(loadBalancer, init);
							activeRules.addAll(sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchB, Knowledge.portDCB, Knowledge.portInB, Knowledge.ipGFB, ipWithoutMask, Knowledge.ipGI, 8888, 8888));
							activeRules.addAll(sdnctlrapi.vnfInTheMiddle(ryu, Knowledge.switchA, Knowledge.portDCA, Knowledge.portInA, Knowledge.ipGFA, ipWithoutMask, Knowledge.ipGI, 8888, 8888));
						}

						Main.logger(this.getClass().getSimpleName(), "Adding gateway to load-balancing pool in DC");
						Vnf gateway = manoapi.addGatewayVnf(vim, Knowledge.ipS, Knowledge.portS, "DC", "gw_"+additionalGWs.size());
						additionalGWs.add(gateway);
						List<EndpointInfo> infos = new ArrayList<>();
						infos.add(gi);
						for(Vnf gw : additionalGWs){
							infos.add(new EndpointInfo(gw.mnIP(),8888));
						}
						Main.logger(this.getClass().getSimpleName(), "Configuring  the load-balancer with updated gateways list");
						manoapi.configMultiLoadBalancer(loadBalancer, infos);
						break;
					case "UC0":
						long now = System.currentTimeMillis();

						if(now - lastActionTimestamp > minimalDelayBeforeReset) {
							for (FlowRule rule : activeRules) {
								sdnctlrapi.removeRule(ryu, rule);
								activeRules.clear();
							}

							for (Vnf gw : additionalGWs) {
								manoapi.removeVnf(vim, "DC", gw.getName());
								additionalGWs.clear();
							}

							if (dedicatedGW != null) {
								manoapi.removeVnf(vim, "DC", dedicatedGW.getName());
								dedicatedGW = null;
							}

							if (filter != null) {
								manoapi.removeVnf(vim, "DC", filter.getName());
								filter = null;
							}

							if (loadBalancer != null) {
								manoapi.removeVnf(vim, "DC", loadBalancer.getName());
								loadBalancer = null;
							}
						}
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
	}
}
