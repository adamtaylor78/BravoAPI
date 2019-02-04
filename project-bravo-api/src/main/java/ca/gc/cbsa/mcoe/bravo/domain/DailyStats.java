package ca.gc.cbsa.mcoe.bravo.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

public class DailyStats {

	@Id
	private String id;
	
	private List<PortStats> ports;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PortStats> getPorts() {
		return ports;
	}

	public void setPorts(List<PortStats> ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return "DailyStats [id=" + id + ", ports=" + ports + "]";
	}
	
}
