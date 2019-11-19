package com.jusda.dac.demo.demo.dac;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.Collections;

@Slf4j
public class CustomerStatementInspector implements StatementInspector {


    public String inspect(String sql) {

        String newSql = SQLDataPrivilegeUtils.getInstance().addPrivilege(sql, Collections.emptyMap());

        log.info("原始的sql:{}",sql);
        log.info("附加数据权限的sql:{}",newSql);

        return newSql;
    }
}
