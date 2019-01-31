package ca.gc.cbsa.mcoe.bravo.domain;

import java.util.List;

public class PortStats {

	private String port;
	private List<PortStatsCounts> counts;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public List<PortStatsCounts> getCounts() {
		return counts;
	}
	public void setCounts(List<PortStatsCounts> counts) {
		this.counts = counts;
	}
	
	@Override
	public String toString() {
		return "PortStats [port=" + port + ", counts=" + counts + "]";
	}
	
	
	
}
