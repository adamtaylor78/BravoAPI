package ca.gc.cbsa.mcoe.bravo.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class ProvinceStatsCounts {

	@Field("Mode")
	private String mode;
	private Long count;
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "ProvinceStatsCounts [mode=" + mode + ", count=" + count + "]";
	}
}
