package ca.gc.cbsa.mcoe.bravo.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TravellersCount {

	@JsonInclude(Include.NON_NULL)
	private Long airSecondaryTotal;
	@JsonInclude(Include.NON_NULL)
	private Long airTotal;
	@JsonInclude(Include.NON_NULL)
	private Long landSecondaryTotal;
	@JsonInclude(Include.NON_NULL)
	private Long landTotal;
	private Long total;
	private Long totalSecondary;
	
	public Long getAirSecondaryTotal() {
		return airSecondaryTotal;
	}
	public void setAirSecondaryTotal(Long airSecondaryTotal) {
		this.airSecondaryTotal = airSecondaryTotal;
	}
	public Long getAirTotal() {
		return airTotal;
	}
	public void setAirTotal(Long airTotal) {
		this.airTotal = airTotal;
	}
	public Long getLandSecondaryTotal() {
		return landSecondaryTotal;
	}
	public void setLandSecondaryTotal(Long landSecondaryTotal) {
		this.landSecondaryTotal = landSecondaryTotal;
	}
	public Long getLandTotal() {
		return landTotal;
	}
	public void setLandTotal(Long landTotal) {
		this.landTotal = landTotal;
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
	
}
