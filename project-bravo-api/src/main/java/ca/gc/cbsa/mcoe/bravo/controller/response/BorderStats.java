package ca.gc.cbsa.mcoe.bravo.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Stats for a specific Port of Entry.")
public class BorderStats {

	private List<BorderStatsCounts> stats;
	private AnnualComparisonStats annualComparisonStats;
	@JsonInclude(Include.NON_EMPTY)
	private List<ProvincialComparisonStats> provincialComparisonStats;
	
	public List<BorderStatsCounts> getStats() {
		if (stats == null) {
			stats = new ArrayList<BorderStatsCounts>();
		}
		return stats;
	}
	public void setStats(List<BorderStatsCounts> stats) {
		this.stats = stats;
	}
	public AnnualComparisonStats getAnnualComparisonStats() {
		return annualComparisonStats;
	}
	public void setAnnualComparisonStats(AnnualComparisonStats annualComparisonStats) {
		this.annualComparisonStats = annualComparisonStats;
	}
	public List<ProvincialComparisonStats> getProvincialComparisonStats() {
		if (provincialComparisonStats == null) {
			provincialComparisonStats = new ArrayList<ProvincialComparisonStats>();
		}
		return provincialComparisonStats;
	}
	public void setProvincialComparisonStats(List<ProvincialComparisonStats> provincialComparisonStats) {
		this.provincialComparisonStats = provincialComparisonStats;
	}
	
}
