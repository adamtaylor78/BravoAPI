package ca.gc.cbsa.mcoe.bravo.domain.commercial;

import java.util.List;

public class PortStatsCommercial {

	private String port;
	private List<PortStatsCountsCommercial> counts;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public List<PortStatsCountsCommercial> getCounts() {
		return counts;
	}
	public void setCounts(List<PortStatsCountsCommercial> counts) {
		this.counts = counts;
	}
	
	@Override
	public String toString() {
		return "PortStats [port=" + port + ", counts=" + counts + "]";
	}
	
}
