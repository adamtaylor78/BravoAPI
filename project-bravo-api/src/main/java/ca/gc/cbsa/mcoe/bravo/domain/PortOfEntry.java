package ca.gc.cbsa.mcoe.bravo.domain;

public class PortOfEntry {

	private String portWorkLocationCode;
	private String portWorkLocationName;
	private String regionalWorkLocationCode;
	private String regionalWorkLocationName;
	private String districtWorkLocationCode;
	private String districtWorkLocationName;
	private String addressLineOne;
	private String addressLineTwo;
	private String municipalityName;
	private String province;
	private String postalCode;
	
	public PortOfEntry() {
		
	}
	
	public PortOfEntry(String portWorkLocationCode, String portWorkLocationName, String regionalWorkLocationCode,
			String regionalWorkLocationName, String districtWorkLocationCode, String districtWorkLocationName,
			String addressLineOne, String addressLineTwo, String municipalityName, String province, String postalCode) {
		super();
		this.portWorkLocationCode = portWorkLocationCode;
		this.portWorkLocationName = portWorkLocationName;
		this.regionalWorkLocationCode = regionalWorkLocationCode;
		this.regionalWorkLocationName = regionalWorkLocationName;
		this.districtWorkLocationCode = districtWorkLocationCode;
		this.districtWorkLocationName = districtWorkLocationName;
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.municipalityName = municipalityName;
		this.province = province;
		this.postalCode = postalCode;
	}
	
	public String getPortWorkLocationCode() {
		return portWorkLocationCode;
	}
	public void setPortWorkLocationCode(String portWorkLocationCode) {
		this.portWorkLocationCode = portWorkLocationCode;
	}
	public String getPortWorkLocationName() {
		return portWorkLocationName;
	}
	public void setPortWorkLocationName(String portWorkLocationName) {
		this.portWorkLocationName = portWorkLocationName;
	}
	public String getRegionalWorkLocationCode() {
		return regionalWorkLocationCode;
	}
	public void setRegionalWorkLocationCode(String regionalWorkLocationCode) {
		this.regionalWorkLocationCode = regionalWorkLocationCode;
	}
	public String getRegionalWorkLocationName() {
		return regionalWorkLocationName;
	}
	public void setRegionalWorkLocationName(String regionalWorkLocationName) {
		this.regionalWorkLocationName = regionalWorkLocationName;
	}
	public String getDistrictWorkLocationCode() {
		return districtWorkLocationCode;
	}
	public void setDistrictWorkLocationCode(String districtWorkLocationCode) {
		this.districtWorkLocationCode = districtWorkLocationCode;
	}
	public String getDistrictWorkLocationName() {
		return districtWorkLocationName;
	}
	public void setDistrictWorkLocationName(String districtWorkLocationName) {
		this.districtWorkLocationName = districtWorkLocationName;
	}
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}
	public String getMunicipalityName() {
		return municipalityName;
	}
	public void setMunicipalityName(String municipalityName) {
		this.municipalityName = municipalityName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	
}
