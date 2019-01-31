package ca.gc.cbsa.mcoe.bravo.domain;

import java.util.List;

public class ProvinceStats {

	private String provinceCode;
	private List<PortStatsCounts> counts;
	
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public List<PortStatsCounts> getCounts() {
		return counts;
	}
	public void setCounts(List<PortStatsCounts> counts) {
		this.counts = counts;
	}
	
	@Override
	public String toString() {
		return "ProvinceStats [port=" + provinceCode + ", counts=" + counts + "]";
	}
	
}
