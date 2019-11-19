package com.jusda.dac.demo.demo.dac;
import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Privilege {
    private String tableName;
    private String colName;
    private String operate;
    private String value;
    private Pattern pattern = Pattern.compile("\\{.*\\}");
    public String toString(String aliasName, Map<String, String> varMap) {
        if (aliasName == null || "".equals(aliasName)) {
            aliasName = tableName + ".";
        } else {
            aliasName = aliasName + ".";
        }
        String sqlString = aliasName + colName + " " + operate + " " + value;
        Matcher m = pattern.matcher(value);
        if (m.find()) {
            String var = m.group().replaceAll("(\\{|\\})", "");
            if (varMap.containsKey(var)) {
                sqlString = sqlString.replaceAll("(\\{.*\\})", varMap.get(var));
            } else {
                System.out.println("缺少信息");
                throw new RuntimeException("缺少必要信息");
            }
        }
        return sqlString;
    }
}