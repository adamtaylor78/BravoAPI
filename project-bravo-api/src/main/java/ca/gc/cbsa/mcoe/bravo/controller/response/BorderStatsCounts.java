package ca.gc.cbsa.mcoe.bravo.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BorderStatsCounts {

	private String timestamp;
	@JsonInclude(Include.NON_NULL)
	private CommercialCount conveyances;
	@JsonInclude(Include.NON_NULL)
	private TravellersCount travellers;
	@JsonInclude(Include.NON_NULL)
	private TravellersCount vehicles;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public CommercialCount getConveyances() {
		return conveyances;
	}
	public void setConveyances(CommercialCount conveyances) {
		this.conveyances = conveyances;
	}
	public TravellersCount getTravellers() {
		return travellers;
	}
	public void setTravellers(TravellersCount travellers) {
		this.travellers = travellers;
	}
	public TravellersCount getVehicles() {
		return vehicles;
	}
	public void setVehicles(TravellersCount vehicles) {
		this.vehicles = vehicles;
	}
	
}
