package ca.gc.cbsa.mcoe.bravo.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProvincialComparisonStats {

	private String provinceCode;
	@JsonInclude(Include.NON_NULL)
	private Long conveyances;
	@JsonInclude(Include.NON_NULL)
	private Long travellers;
	@JsonInclude(Include.NON_NULL)
	private Long vehicles;
	
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public Long getConveyances() {
		return conveyances;
	}
	public void setConveyances(Long conveyances) {
		this.conveyances = conveyances;
	}
	public Long getTravellers() {
		return travellers;
	}
	public void setTravellers(Long travellers) {
		this.travellers = travellers;
	}
	public Long getVehicles() {
		return vehicles;
	}
	public void setVehicles(Long vehicles) {
		this.vehicles = vehicles;
	}
	
	
}
