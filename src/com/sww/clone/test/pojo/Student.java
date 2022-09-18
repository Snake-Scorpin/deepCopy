package com.sww.clone.test.pojo;

import java.io.Serializable;

/**
*
* @author Snake_Scorpion
* @date 2022年9月2日
* @version V22.09
*/
public class Student implements Cloneable ,Serializable{
	/**
	 * id
	 */
	private static final long serialVersionUID = -5273999018659202469L;
	private Integer id;
	private String name;
	private int age;
	private Teacher teacher;
	
	@Override
	public Student clone()  {
		try {
			return (Student) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}


	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", teacher=" + teacher + "]";
	}
	
	
	
}
