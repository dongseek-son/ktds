package com.ktds.common.dao.support;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BindData {

   public static <T> T bindData(ResultSet rs, T obj) {
      
      Field[] fields = obj.getClass().getDeclaredFields();
      Method[] methods = obj.getClass().getDeclaredMethods();
      
      String column = "";
      String fieldName = "";
      boolean requires = false;
      Class<?> fieldType = null;
      
      Annotation[] annotataions = null;
      for (Field field : fields) {
         annotataions = field.getDeclaredAnnotations();
         fieldName = field.getName();
         fieldType = field.getType();
         for (Annotation annotation : annotataions) {
            if ( annotation.annotationType() == Types.class ) {
               requires = ((Types) annotation).requires();
               column = ((Types) annotation).alias();
               try {
                  if(column.length() > 0) {
                     rs.getString(column);
                  }
                  else {
                     column = convertString(fieldName);
                  }
               }
               catch(Exception e) {
                  column = convertString(fieldName);
               }
            }
            
            for (Method method : methods) {
               if(method.getName().toUpperCase().equals("SET" + fieldName.toUpperCase())) {
                  if(fieldType == int.class) {
                     try {
                        method.invoke(obj, rs.getInt(column));
                     } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        if(requires) {
                           throw new RuntimeException(e.getMessage(), e);
                        }
                     }
                  }
                  else if ( fieldType == double.class ) {
                     try {
                        method.invoke(obj, rs.getDouble(column));
                     } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        if(requires) {
                           throw new RuntimeException(e.getMessage(), e);
                        }
                     }
                  }
                  else if ( fieldType == String.class ) {
                     try {
                        method.invoke(obj, rs.getString(column));
                     } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        if(requires) {
                           throw new RuntimeException(e.getMessage(), e);
                        }
                     }
                  }
               }
            }
            
         }
      }
      
      return obj;
      
   }
   
   private static String convertString(String camelCase) {
      String smallLetters = "abcdefghijklmnopqrstuvwxyz";
      String bigLetters = "ABCEDFGHIJKLMNOPWRSTUVWXYZ";
      String column = "";

      char[] fieldLetters = camelCase.toCharArray();
      for (char c : fieldLetters) {
         if (smallLetters.indexOf(c + "") != -1) {
            column += (c + "").toUpperCase();
         } else if (bigLetters.indexOf(c + "") != -1) {
            column += ("_" + c);
         }
      }
      
      return column;
   }
   
}