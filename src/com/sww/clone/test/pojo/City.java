package com.sww.clone.test.pojo;
/**
*
* @author Snake_Scorpion
* @date 2022年9月7日
* @version V22.09
*/
public class City {
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + "]";
	}
	
}
