package api.middleware;

public class HealthResponse {

	private double avgload;
	private double currentload;

	public HealthCPU[] cpus;

	public double getCurrentload() {
		return currentload;
	}

	public void setCurrentload(double currentload) {
		this.currentload = currentload;
	}

	public HealthResponse() {
	}

	/**
	 * @return the avgload
	 */
	public double getAvgload() {
		return avgload;
	}

	/**
	 * @param avgload the avgload to set
	 */
	public void setAvgload(double avgload) {
		this.avgload = avgload;
	}

	/**
	 * @return the cpus
	 */
	public HealthCPU[] getCpus() {
		return cpus;
	}

	/**
	 * @param cpus the cpus to set
	 */
	public void setCpus(HealthCPU[] cpus) {
		this.cpus = cpus;
	}

}
