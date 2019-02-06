package ca.gc.cbsa.mcoe.bravo.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AnnualComparisonStats {

	@JsonInclude(Include.NON_EMPTY)
	private List<Long> conveyances;
	@JsonInclude(Include.NON_EMPTY)
	private List<Long> travellers;
	@JsonInclude(Include.NON_EMPTY)
	private List<Long> vehicles;
	
	public List<Long> getConveyances() {
		if (conveyances == null) {
			conveyances= new ArrayList<Long>();
		}
		return conveyances;
	}
	public void setConveyances(List<Long> conveyances) {
		this.conveyances = conveyances;
	}
	public List<Long> getTravellers() {
		if (travellers == null) {
			travellers= new ArrayList<Long>();
		}
		return travellers;
	}
	public void setTravellers(List<Long> travellers) {
		this.travellers = travellers;
	}
	public List<Long> getVehicles() {
		if (vehicles == null) {
			vehicles = new ArrayList<Long>();
		}
		return vehicles;
	}
	public void setVehicles(List<Long> vehicles) {
		this.vehicles = vehicles;
	}
	
}
