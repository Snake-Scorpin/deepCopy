package com.sww.clone.test.pojo;

import java.io.Serializable;

/**
*
* @author Snake_Scorpion
* @date 2022年9月2日
* @version V22.09
*/
public class Teacher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1972456516482668917L;
	private int id;
	private String name;
	private String grade;//年级
	private School school;
	
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", grade=" + grade + ", school=" + school + "]";
	}
	
	
	
}
