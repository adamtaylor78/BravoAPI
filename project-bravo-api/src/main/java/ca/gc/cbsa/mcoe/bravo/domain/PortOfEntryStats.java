package ca.gc.cbsa.mcoe.bravo.domain;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Stats for a specific Port of Entry.")
public class PortOfEntryStats {

	private String timestamp;
	private Long total;
	private Long totalSecondary;
	private Long airTotal;
	private Long landTotal;
	private Long airSecondaryTotal;
	private Long landSecondaryTotal;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getTotalSecondary() {
		return totalSecondary;
	}
	public void setTotalSecondary(Long totalSecondary) {
		this.totalSecondary = totalSecondary;
	}
	public Long getAirTotal() {
		return airTotal;
	}
	public void setAirTotal(Long airTotal) {
		this.airTotal = airTotal;
	}
	public Long getLandTotal() {
		return landTotal;
	}
	public void setLandTotal(Long landTotal) {
		this.landTotal = landTotal;
	}
	public Long getAirSecondaryTotal() {
		return airSecondaryTotal;
	}
	public void setAirSecondaryTotal(Long airSecondaryTotal) {
		this.airSecondaryTotal = airSecondaryTotal;
	}
	public Long getLandSecondaryTotal() {
		return landSecondaryTotal;
	}
	public void setLandSecondaryTotal(Long landSecondaryTotal) {
		this.landSecondaryTotal = landSecondaryTotal;
	}
	
}
