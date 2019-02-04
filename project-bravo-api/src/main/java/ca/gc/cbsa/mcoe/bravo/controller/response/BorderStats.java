package ca.gc.cbsa.mcoe.bravo.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Stats for a specific Port of Entry.")
public class BorderStats {

	private String timestamp;
	private Long total;
	@JsonInclude(Include.NON_NULL)
	private Long totalSecondary;
	@JsonInclude(Include.NON_NULL)
	private Long airTotal;
	@JsonInclude(Include.NON_NULL)
	private Long landTotal;
	@JsonInclude(Include.NON_NULL)
	private Long airSecondaryTotal;
	@JsonInclude(Include.NON_NULL)
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
