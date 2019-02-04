package ca.gc.cbsa.mcoe.bravo.controller.response;

import java.util.ArrayList;
import java.util.List;

public class ProvincialStats {

	private List<BorderStats> stats = new ArrayList<BorderStats>();
	private List<ProvincialComparisonStats> provincialComparisonStats = new ArrayList<ProvincialComparisonStats>();
	
	public List<BorderStats> getStats() {
		return stats;
	}
	public void setStats(List<BorderStats> stats) {
		this.stats = stats;
	}
	public List<ProvincialComparisonStats> getProvincialComparisonStats() {
		return provincialComparisonStats;
	}
	public void setProvincialComparisonStats(List<ProvincialComparisonStats> provincialComparisonStats) {
		this.provincialComparisonStats = provincialComparisonStats;
	}
	
}
