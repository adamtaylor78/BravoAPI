package ca.gc.cbsa.mcoe.bravo.domain.travellers;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Daily")
public class DailyStatsTravellers {

	@Id
	private String id;
	
	private List<PortStatsTravellers> ports;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PortStatsTravellers> getPorts() {
		return ports;
	}

	public void setPorts(List<PortStatsTravellers> ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return "DailyStats [id=" + id + ", ports=" + ports + "]";
	}
	
}
