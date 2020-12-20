package api.middleware;

public class HealthResponse {
	private double currentload;

	public double getCurrentload() {
		return currentload;
	}

	public void setCurrentload(double currentload) {
		this.currentload = currentload;
	}

	public HealthResponse() {
	}
}
