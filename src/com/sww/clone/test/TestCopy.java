package com.sww.clone.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sww.clone.test.pojo.School;
import com.sww.clone.test.pojo.Student;
import com.sww.clone.test.pojo.Teacher;
import com.sww.clone.util.CopyUtil;

/**
 *
 * @author Snake_Scorpion
 * @date 2022年9月2日
 * @version V22.09
 */
public class TestCopy {
	// 引用赋值，不会拷贝对象
	@Test
	public void copy1() {
		Student stu1 = new Student();
		stu1.setId(1);
		stu1.setAge(18);
		stu1.setName("刘耀琦");
		Student stu2 = null;
		stu2 = stu1;
		stu1.setName("刘耀琦2");
		System.out.println(stu1 == stu2);
		System.out.println("stu1:" + stu1.getName());
		System.out.println("stu2:" + stu2.getName());
	}

	// 使用java克隆API（1、实现 cloneable接口；2、重写Object的clone()）
	// 这是一种浅拷贝：
	// 克隆/copy/拷贝对象时，只复制基本类型的属性，引用类型的属性采用的是地址赋值的方式
	@Test
	public void copy2() {
		Student stu1 = new Student();
		stu1.setId(1);
		stu1.setAge(18);
		stu1.setName("刘耀琦");
		Teacher tea = new Teacher();
		tea.setId(1);
		tea.setName("孙");
		tea.setGrade("三年级");
		stu1.setTeacher(tea);

		Student stu2 = stu1.clone();
		System.out.println("stu1==stu2:" + (stu1 == stu2));
		System.out.println("stu1.getTeacher()==stu2.getTeacher():" + (stu1.getTeacher() == stu2.getTeacher()));

	}

	// 深拷贝：对象中引用类型的属性也进行复制
	// 利用反射
	// 多次使用clone()
	// 序列化、反序列化
	// 利用Jackson等工具进行序列化
	/*
	 * json格式： 对 象用大括号（{}）包裹； 属性名和属性值用（""）包裹，数值类型、对象可以不加双引号； 属性与属性之间用逗号（,）分隔
	 * 集合类型用（[]）包裹 对象与对象之间用逗号（,）分隔
	 */
	// 1、jackson
	@Test
	public void copy3Jackson() {
		// 创建学生对象stu1 并为其属性赋值
		Student stu1 = new Student();
		stu1.setId(1);
		stu1.setAge(18);
		stu1.setName("刘耀琦");
		// 创建老师对象
		Teacher tea = new Teacher();
		tea.setId(1);
		tea.setName("孙");
		tea.setGrade("三年级");
		// 将老师对象赋值给学生对象的teacher属性
		stu1.setTeacher(tea);
		/*
		 * 克隆
		 */
		// 创建ObjectMapper对象，jackson框架提供的.
		// 封装了一些方法用来将对象转json格式字符串以及将json格式字符串转对象
		ObjectMapper om = new ObjectMapper();
		Student stu2 = null;// 空的克隆体
		try {
			// writeValueAsString()把一个对象转成json格式的字符串
			String str = om.writeValueAsString(stu1);
			System.out.println("str:" + str);// 输出json字符串
			// readValue(str,class)把json字符串转成class（指定类型的反射对象）类型对象，
			stu2 = om.readValue(str, Student.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("stu1==stu2:" + (stu1 == stu2));
		System.out.println("stu1.getTeacher()==stu2.getTeacher():" + (stu1.getTeacher() == stu2.getTeacher()));

	}

	// 2、序列化、反序列化(io)
	@Test
	public void copy3Serializable() {
		// 创建学生对象stu1 并为其属性赋值
		Student stu1 = new Student();
		stu1.setId(1);
		stu1.setAge(18);
		stu1.setName("刘耀琦");
		// 创建老师对象
		Teacher tea = new Teacher();
		tea.setId(1);
		tea.setName("孙");
		tea.setGrade("三年级");
		// 将老师对象赋值给学生对象的teacher属性
		stu1.setTeacher(tea);

		// 克隆
		Student stu2 = null;
		try {
			// 创建输出流，将对象传入
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(stu1);
			// 创建输入流，将对象导出
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bin);
			stu2 = (Student) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("stu1==stu2:" + (stu1 == stu2));
		System.out.println("stu1.getTeacher()==stu2.getTeacher():" + (stu1.getTeacher() == stu2.getTeacher()));

	}

	@Test
	public void testReflectCopy() {
		// 创建学生对象stu1 并为其属性赋值
		Student stu1 = new Student();
		stu1.setId(1);
		stu1.setAge(18);
		stu1.setName("刘耀琦");
		// 创建老师对象
		Teacher tea = new Teacher();
		tea.setId(1);
		tea.setName("孙");
		tea.setGrade("1年级");
		//学校
		School school=new School();
		school.setId(1);
		school.setName("bdqn");
		tea.setSchool(school);
		//城市
//		City city=new City();
//		city.setId(1);
//		city.setName("上海");
//		school.setCity(city);
		// 将老师对象赋值给学生对象的teacher属性
		stu1.setTeacher(tea);

		// 克隆
		CopyUtil<Student> copy=new CopyUtil<>();
		Student stu2 = copy.deepCopy(stu1);
		System.out.println("stu1:"+stu1);
		System.out.println("stu2:"+stu2);
		System.out.println("stu1==stu2:" + (stu1 == stu2));
		System.out.println("teacher==teacher:"+(stu1.getTeacher()==stu2.getTeacher()));
		System.out.println("shcool==shcool:"+(stu1.getTeacher().getSchool()==stu2.getTeacher().getSchool()));
		System.out.println("city==city:"+(stu1.getTeacher().getSchool().getCity()==stu2.getTeacher().getSchool().getCity()));
	}

	
}
