package ca.gc.cbsa.mcoe.bravo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "divisions")
public class Division {

	@Id
	private Integer code;
	private String name;
	
	public Division(Integer code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	
	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
