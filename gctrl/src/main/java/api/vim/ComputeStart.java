package api.vim;

public class ComputeStart {
	private String image = null;
	private String docker_command = null;

	public ComputeStart() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDocker_command() {
		return docker_command;
	}

	public void setDocker_command(String docker_command) {
		this.docker_command = docker_command;
	}
}
