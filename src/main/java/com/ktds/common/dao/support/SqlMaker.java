package com.ktds.common.dao.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.modeler.FeatureInfo;

public class SqlMaker {
	
	private static class FieldData {
		private List<String> fieldNameList;
		private List<Class<?>> fieldTypeList;
		private List<Object> fieldValueList;  
		
		public List<String> getFieldNameList() {
			return fieldNameList;
		}

		public List<Class<?>> getFieldTypeList() {
			return fieldTypeList;
		}
		
		public List<Object> getFieldValueList() {
			return fieldValueList;
		}

		public FieldData(List<String> fieldNameList, List<Class<?>> fieldTypeList, List<Object> fieldValueList) {
			this.fieldNameList = fieldNameList;
			this.fieldTypeList = fieldTypeList;
			this.fieldValueList = fieldValueList;
		}
	}
	
	public static String createInsertQuery( Object obj, String tableName, String notBind ) {
		FieldData fieldData = makeFieldDataList(obj);
		List<String> fieldNameList = fieldData.getFieldNameList();
		List<Class<?>> fieldTypeList = fieldData.getFieldTypeList();
		List<Object> fieldValueList = fieldData.getFieldValueList();
		Map<String, String> notBindMap = new HashMap<String, String>();
		
		if ( notBind != null ) {
			String[] notBindArray = notBind.split(",");
			for( String str : notBindArray ) {
				String[] columnInfo = str.split("=");
				notBindMap.put(columnInfo[0].toUpperCase(), columnInfo[1]);
			}
		}
		
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO " + tableName + " (");
		
		for ( int i = 0; i < fieldNameList.size() ; i++ ) {
			query.append(fieldNameList.get(i));
			if( i < fieldNameList.size()-1 ) {
				query.append(',');
			}
		}
		query.append(") VALUES (");
		for ( int i = 0; i < fieldNameList.size() ; i++ ) {	
			
			if( notBindMap.get(fieldNameList.get(i)) != null) {
				query.append(notBindMap.get(fieldNameList.get(i)));
			}
			else {
				if ( fieldTypeList.get(i) == String.class ) {
					query.append("'" + fieldValueList.get(i) + "'");
				}
				else {
					query.append(fieldValueList.get(i));
				}
			}
			
			if( i < fieldNameList.size()-1 ) {
				query.append(',');
			}
		}
		query.append(')');
		
		return query.toString().toUpperCase();
	}
	
	private static FieldData makeFieldDataList( Object obj ) {
		Field[] fields = obj.getClass().getDeclaredFields();
		Method[] methods = obj.getClass().getDeclaredMethods();
		
		List<String> fieldNameList = new ArrayList<String>();
		List<Class<?>> fieldTypeList = new ArrayList<Class<?>>();
		List<Object> fieldValueList = new ArrayList<Object>();
	
	    String fieldName = "";
	    Class<?> fieldType = null;
	    Object fieldValue = null;
		
	      for (Field field : fields) {

	         fieldName = field.getName().toUpperCase();
	         fieldType = field.getType();
	         
	         fieldNameList.add(fieldName);
	         fieldTypeList.add(fieldType);
	         for (Method method : methods) {
	               if(method.getName().toUpperCase().equals("GET" + fieldName.toUpperCase())) {
	            	   try {
	            		   fieldValue = method.invoke(obj);
	            		  fieldValueList.add(fieldValue);
	            	   } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	            		   throw new RuntimeException(e.getMessage(), e);
	            	   }
	               }
	         }
	         System.out.println("fieldName = " + fieldName);
	         System.out.println("fieldType = " + fieldType);
	         System.out.println("fieldValue = " + fieldValue);
	      }
	      return new FieldData(fieldNameList, fieldTypeList, fieldValueList);
	}
   
}