package com.jusda.dac.demo.demo.dac;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DACContextHolder {
    private static ThreadLocal<Map<String,String>> threadLocal=ThreadLocal.withInitial(()->new HashMap<>());
     
    public static Map<String,String> get(){
         return threadLocal.get();
    }
     
    public static void set(String key,String value) {
         Map<String,String> map=get();
         
         map.put(key, value);
    }
    public static void set(String key,List<String> list,boolean isVarchar) {
         Map<String,String> map=get();
         StringBuilder sb=new StringBuilder(128);
         sb.append(" ( ");
         for(String str:list) {
             if(isVarchar) {
                 sb.append("'");
                 sb.append(str);
                 sb.append("',");
             }else {
                 sb.append(str);
                 sb.append(",");
             }
         }
         sb.deleteCharAt(sb.length()-1);
         sb.append(" ) ");
          
         map.put(key, sb.toString());
    }
  
}