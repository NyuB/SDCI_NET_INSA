import java.util.List;

//

// @author couedrao on 25/11/2019.

//* @project gctrl

//

//

//* 1)Structures the actions needed to achieve goals and objectives Structures the actions needed to achieve goals and objectives.

//* 2)The plan function creates or selects a procedure to enact a desired alteration in the managed resource.

//* 3)The plan function can take on many forms, ranging from a single command to a complex workflow.

//*

@SuppressWarnings({"SynchronizeOnNonFinalField"})
class Plan {
	private int nextPlan = 0;
	public String gw_PLAN = "";
	private int nbSymptoms = 0;

	void start() {
		Main.logger(this.getClass().getSimpleName(), "Start Planning");

		while (Main.run) {
			String current_rfc = get_rfc();
			//Main.logger(this.getClass().getSimpleName(), "Received RFC : " + current_rfc);
			update_plan(plan_generator(current_rfc));

		}
	}

	//RFC Receiver
	private String get_rfc() {
		synchronized (Main.analyze.gw_current_RFC) {
			try {
				Main.analyze.gw_current_RFC.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Main.analyze.gw_current_RFC;
	}

	//Rule-based Plan Generator
	private String plan_generator(String rfc) {
		List<String> rfcs = Main.shared_knowledge.get_rfc();
		List<String> plans = Main.shared_knowledge.get_plans();

		if ("YourPlansDoNotWork".contentEquals(rfc)) {
			// Thread.sleep(2000);
			//Main.run = false;
			//Main.logger(this.getClass().getSimpleName(), "All the Plans were executed without success. \n \t\t The loop will stop!");
			Main.logger(this.getClass().getSimpleName(), "All the Plans were executed without success :( . \n \t\t Wait and hope it is only a matter of time!");
			return plans.get(0);//Just wait instead of exiting immediatly
			// Terminate JVM
			//System.exit(0);
		} else if (rfc.contentEquals(rfcs.get(0))) { // Wait
			nbSymptoms = 0;
			Main.logger(this.getClass().getSimpleName(), "No symptom detected, everything is OK");
			Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(0));
			//nextPlan = 0; Don't reset the plans until actual backward actions(e.g undeploying vnf) are implemented
			return plans.get(0);
		} else if (rfc.contentEquals(rfcs.get(1))) { // Act
			nbSymptoms++;
			boolean symptom_ko = nbSymptoms > 2;
			Main.logger(this.getClass().getSimpleName(), "Consecutive alert symptoms : " + nbSymptoms);
			if (symptom_ko) {
				if (nextPlan < 4) {
					nextPlan++;
					nbSymptoms = 0;
					Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(1));
					return plans.get(1);
				} else { //Insert other plans here
					Main.logger(this.getClass().getSimpleName(), "No more plans available...wait and pray !");
					Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(0));
					return plans.get(0);
				}
			} else {
				Main.logger(this.getClass().getSimpleName(), "Wait and watch before executing plan");
				Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(0));
				return plans.get(0);
			}
		}
		else if(rfc.contentEquals(rfcs.get(2))){ // Reset
			nbSymptoms = 0;
			nextPlan = 0;
			Main.logger(this.getClass().getSimpleName(), "System in very good shape, resource reallocation possible");
			Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(plans.size()-1));
			return plans.get(plans.size()-1);//Last plan should be the reset plan
		}
		return null;
	}

	private void update_plan(String plan) {
		synchronized (gw_PLAN) {
			gw_PLAN.notify();
			gw_PLAN = plan;
		}
	}
}

