

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用反射实现的深拷贝框架，所有自定义类型的属性均会自动拷贝 
 * 数组、集合类型暂未考虑
 * 
 * @author Snake_Scorpion
 * @date 2022年9月7日
 * @version V22.09
 */
public class CopyUtil<T> {
	/*
	 * 工具方法------------------------------------------------------------
	 */

	// 利用反射进行对象的复制。
	public T deepCopy(Object object) {
		Object objCloned = null;
		Class objCloneClazz = null;
		// 获取object的class对象
		Class stuClazz = object.getClass();// 所传入参数的class对象（非Object）
		// 判断是否有clone()方法，有直接调用进行浅拷贝
		if (hasClone(stuClazz)) {
			try {
				Method cloneMethod = stuClazz.getMethod("clone");// 获取Student中的clone方法
				// cloneMethod.setAccessible(true);//修改成员的可见性
				objCloned = cloneMethod.invoke(object);// 指定object对象作为cloneMethod方法的调用者
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 无clone方法，利用反射克隆
			try {
				objCloned = stuClazz.getConstructor().newInstance();// 获取无参构造
				// 属性赋值（调用objectCloned的set和object的get方法）
				objCloneClazz = objCloned.getClass();
				for (Method method : objCloneClazz.getMethods()) {
					String methodName = method.getName();
					String prefix = methodName.substring(0, 3);
					String suffix = methodName.substring(3);
					if ("set".equals(prefix)) {
						method.invoke(objCloned, stuClazz.getMethod("get" + suffix).invoke(object));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 判断object是否有自定义类型，若无直接return浅拷贝对象
		if (!hasCustomTypeList(stuClazz)) {// 无自定义类型，返回浅拷贝对象
			return (T) objCloned;
		}
		// 若有,进行自定义类型字段的克隆
		try {
			objCloneClazz = objCloned.getClass();
			Map<String, Object> fieldNameAndValue = getCustomValueList(object);
			for (String fieldName : fieldNameAndValue.keySet()) {
				Class type = objCloneClazz.getDeclaredField(fieldName).getType();
				// 获取set方法
				Method setObj = objCloneClazz.getMethod("set" + upperCaseFirstLetter(fieldName), type);
				if (null == fieldNameAndValue.get(fieldName)) {
					// 调用set方法
					setObj.invoke(objCloned, new Object[]{null});
				}else{
					// 利用递归（自己调用自己）进行深一层的拷贝
					Object obj = deepCopy(fieldNameAndValue.get(fieldName));
					// 调用set方法
					setObj.invoke(objCloned, obj);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (T) objCloned;
	}

	// 获取某个类型中自定义类型的字段值集合
	private Map<String, Object> getCustomValueList(Object object) throws Exception {
		Map<String, Object> fieldNameAndValue = new HashMap();
		Class clazz = object.getClass();
		if (!hasCustomTypeList(clazz)) {// object不包含自定义类型
			throw new Exception("此对象不包含自定义类");
		}
		for (Field field : clazz.getDeclaredFields()) {
			if (isCustomType(field)) {
				// 获取关联的get方法
				Method method = clazz.getMethod("get" + upperCaseFirstLetter(field.getName()));
				// 调用get方法获取属性值
				Object obj = method.invoke(object);
				fieldNameAndValue.put(field.getName(), obj);
			}
		}
		return fieldNameAndValue;
	}

	// 字符串首字母转大写
	private String upperCaseFirstLetter(String str) {
		String firstLetter = str.substring(0, 1);// 含0不含1，只截取第一个
		String others = str.substring(1);// 从1号位开始截取到最后
		return firstLetter.toUpperCase() + others;
	}

	// 判断某个类型是否有clone方法
	private boolean hasClone(Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if ("clone".equals(method.getName()))
				return true;
		}
		return false;
	}

	// 判断一个类型的字段是否有引用类型
	private boolean hasCustomTypeArray(Class clazz) {
		String[] names = { "byte", "java.lang.Byte", "int", "java.lang.Integer", "short", "java.lang.Short", "long",
				"java.lang.Long", "char", "java.lang.Character", "boolean", "java.lang.Boolean", "float",
				"java.lang.Float", "double", "java.lang.Double", "java.lang.String" };
		for (Field field : clazz.getDeclaredFields()) {
			boolean flag = false;
			for (String name : names) {
				String typeName = field.getGenericType().getTypeName();
				// System.out.println("typeName:"+typeName);
				if (name.equals(typeName)) {// field为原始类型
					flag = true;
				}
			}
			if (flag == false) {
				return true;// 有自定义类型
			}
		}
		return false;
	}

	private boolean hasCustomTypeList(Class clazz) {
		String[] names = { "byte", "java.lang.Byte", "int", "java.lang.Integer", "short", "java.lang.Short", "long",
				"java.lang.Long", "char", "java.lang.Character", "boolean", "java.lang.Boolean", "float",
				"java.lang.Float", "double", "java.lang.Double", "java.lang.String" };
		List list = Arrays.asList(names);// 数组转集合对象
		for (Field field : clazz.getDeclaredFields()) {
			if (!(list.contains(field.getGenericType().getTypeName()))) {
				return true;
			}
		}
		return false;
	}

	// 判断某个字段是否是自定义类型
	private boolean isCustomType(Field field) {
		String[] names = { "byte", "java.lang.Byte", "int", "java.lang.Integer", "short", "java.lang.Short", "long",
				"java.lang.Long", "char", "java.lang.Character", "boolean", "java.lang.Boolean", "float",
				"java.lang.Float", "double", "java.lang.Double", "java.lang.String" };
		List list = Arrays.asList(names);// 数组转集合对象
		if (!(list.contains(field.getGenericType().getTypeName()))) {
			return true;
		}
		return false;
	}

}
