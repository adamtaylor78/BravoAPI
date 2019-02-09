package ca.gc.cbsa.mcoe.bravo.domain.travellers;

import java.util.List;

public class PortStatsTravellers {

	private String port;
	private List<PassageCounts> passageCounts;
	private List<ReferralCounts> referralCounts;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public List<PassageCounts> getPassageCounts() {
		return passageCounts;
	}
	public void setPassageCounts(List<PassageCounts> passageCounts) {
		this.passageCounts = passageCounts;
	}
	public List<ReferralCounts> getReferralCounts() {
		return referralCounts;
	}
	public void setReferralCounts(List<ReferralCounts> referralCounts) {
		this.referralCounts = referralCounts;
	}
	@Override
	public String toString() {
		return "PortStatsTravellers [port=" + port + ", passageCounts=" + passageCounts + ", referralCounts="
				+ referralCounts + "]";
	}
	
	
}
