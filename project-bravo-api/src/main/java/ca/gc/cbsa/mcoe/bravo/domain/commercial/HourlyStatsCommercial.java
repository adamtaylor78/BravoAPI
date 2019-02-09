package ca.gc.cbsa.mcoe.bravo.domain.commercial;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Hourly")
public class HourlyStatsCommercial {
	
	@Id
	private String id;
	
	private List<PortStatsCommercial> ports;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PortStatsCommercial> getPorts() {
		return ports;
	}

	public void setPorts(List<PortStatsCommercial> ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return "HourlyStats [id=" + id + ", ports=" + ports + "]";
	}
	
	
}
