package com.jusda.dac.demo.demo.dac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;

/**
 * Hello world!
 */
public class SQLDataPrivilegeUtils {
    public static void main(String[] args) {
        DACContextHolder.set("nums.id", "3");
        List<String> list = new ArrayList<>();
        list.add("0342e276-c046-40f0-be4a-1a04092bc7dd");
        list.add("090493b4-aa59-4680-a707-907cb139ebd1");
        list.add("098806d5-c07a-46c9-8901-e50045466455");
        list.add("0bbfa1ed-9ec2-4e9e-9dfd-181945cb5eed");
        list.add("0bd9528a-447b-4dc3-950a-4a45e066f22d");
        DACContextHolder.set("kylin_query_log.cube_id", list, true);
        DACContextHolder.set("kylin_cube.id", List.of("1","2","3"),false);
        SQLDataPrivilegeUtils a = new SQLDataPrivilegeUtils();
        String sql = "select t4.dt,ifnull(t3.value,0) value from\r\n" +
                "        (\r\n" +
                "            select '2018-07-28'+interval (id-1) day dt from nums where id<=datediff('2018-08-03','2018-07-28')+1\r\n" +
                "        ) t4\r\n" +
                "        left join\r\n" +
                "        (\r\n" +
                "            select * from (\r\n" +
                "                select date_format(query_time,'%Y-%m-%d') dt,cast(sum(result) as SIGNED) value from kylin_result_cache_by_cubeid\r\n" +
                "                where\r\n" +
                "                module='query_total_by_cubeid' and\r\n" +
                "                cube_id in (select id from kylin_cube where is_delete=0) and\r\n" +
                "                 \r\n" +
                "        query_time< date_format('2018-08-03'+interval 1 day,'%Y-%m-%d 00:00:00') and  query_time>= date_format('2018-07-28','%Y-%m-%d 00:00:00')\r\n" +
                "         \r\n" +
                "     \r\n" +
                "                group by date_format(query_time,'%Y-%m-%d')\r\n" +
                "            ) t1 union all\r\n" +
                "\r\n" +
                "                select date_format(query_time,'%Y-%m-%d') dt,count(*) value from kylin_query_log\r\n" +
                "                where\r\n" +
                "                cube_id in (select id from kylin_cube where is_delete=0) and\r\n" +
                "                 \r\n" +
                "        query_time>=date_format(now() ,'%Y-%m-%d 00:00:00')\r\n" +
                "        and query_time<date_format(now()+interval 1 day ,'%Y-%m-%d 00:00:00')\r\n" +
                "         \r\n" +
                "     \r\n" +
                "                group by date_format(query_time,'%Y-%m-%d')\r\n" +
                "\r\n" +
                "        ) t3\r\n" +
                "        on(t3.dt=t4.dt)\r\n" +
                "        where t4.dt between '2018-07-28' and '2018-08-03'\r\n" +
                "        order by t4.dt\r\n" +
                "";
        a.addPrivilege(sql, null);
    }


    //单例.该对象用于给已经存在的SQL增加数据权限
    private static SQLDataPrivilegeUtils INSTANCE = new SQLDataPrivilegeUtils();

    public static SQLDataPrivilegeUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 获取权限配置,这里暂时先写死
     */
    //从数据库中获取配置信息
    private SQLDataPrivilegeUtils() {
        Privilege p1 = new Privilege();
        p1.setTableName("nums");
        p1.setColName("id");
        p1.setOperate("<=");
        p1.setValue("{nums.id}");
        privList.add(p1);

        Privilege p2 = new Privilege();
        p2.setTableName("kylin_query_log");
        p2.setColName("cube_id");
        p2.setOperate("in");
        p2.setValue("{kylin_query_log.cube_id}");
        privList.add(p2);

        Privilege p3 = new Privilege();
        p3.setTableName("kylin_query_log");
        p3.setColName("query_time");
        p3.setOperate("<=");
        p3.setValue("now()-interval'1'day");
        privList.add(p3);

        Privilege p4 = new Privilege();
        p4.setTableName("kylin_result_cache_by_cubeid");
        p4.setColName("id");
        p4.setOperate("in");
        p4.setValue("{kylin_cube.id}");
        privList.add(p4);

        Privilege p5 = new Privilege();
        p5.setTableName("kylin_result_cache_by_cubeid");
        p5.setColName("id");
        p5.setOperate("in");
        p5.setValue("{kylin_cube.id}");
        privList.add(p5);

        Privilege p6 = new Privilege();
        p6.setTableName("t_order");
        p6.setColName("created_by");
        p6.setOperate("=");
        p6.setValue("'张三'");
        privList.add(p6);
    }

    //保存本项目的数据权限配置信息
    private List<Privilege> privList = new ArrayList<>();

    //在SQL上拼接数据权限
    public String addPrivilege(final String sql, Map<String, String> varMap) {
        if (varMap == null) {
            varMap = DACContextHolder.get();
        }
        // SQLParserUtils.createSQLStatementParser可以将sql装载到Parser里面
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);
        // parseStatementList的返回值SQLStatement本身就是druid里面的语法树对象
        List<SQLStatement> stmtList = parser.parseStatementList();
        SQLStatement stmt = stmtList.get(0);
        //如果不是查询,则返回
        if (!(stmt instanceof SQLSelectStatement)) {
            return sql;
        }
        SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;
        // 拿到SQLSelect 通过在这里打断点看对象我们可以看出这是一个树的结构
        SQLSelect sqlselect = selectStmt.getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();

        parseSubQuery(query.getSelectList(), varMap);
        parseTable(query, varMap);
        System.out.println(sqlselect.toString());
        return sqlselect.toString();
    }

    /**
     * 给子查询增加数据权限
     * @param fieldList
     * @param varMap
     */
    private void parseSubQuery(final List<SQLSelectItem> fieldList, final Map<String, String> varMap) {
        for (SQLSelectItem item : fieldList) {
            if (item.getExpr() instanceof SQLQueryExpr) {
                SQLQueryExpr expr = (SQLQueryExpr) item.getExpr();
                parseTable(expr.getSubQuery().getQueryBlock(), varMap);
            }
        }
    }

    /**
     * 递归处理嵌套表
     * @param query
     * @param varMap
     */
    private void parseTable(final SQLSelectQueryBlock query, final Map<String, String> varMap) {
        if (query == null) {
            return;
        }
        SQLTableSource tableSource = query.getFrom();
        if (tableSource instanceof SQLExprTableSource) {
            //如果是普通的表,则在where中增加数据权限
            SQLExprTableSource table = ((SQLExprTableSource) tableSource);
            String tableName = table.getName().getSimpleName();
            String aliasName = table.getAlias();
            SQLExpr sqlExpr = createSQLExpr(tableName, aliasName, varMap);
            createWhereSQLExpr(query, varMap, sqlExpr);
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            //如果是嵌套表,则递归到内层
            SQLSubqueryTableSource table = ((SQLSubqueryTableSource) tableSource);
            parseTable(table.getSelect().getQueryBlock(), varMap);
        } else if (tableSource instanceof SQLJoinTableSource) {
            //如果是两个表关联.则在on条件中增加数据权限。并且在左右表中分别判断是否是union all的情况
            SQLJoinTableSource table = ((SQLJoinTableSource) tableSource);
            SQLTableSource left = table.getLeft();
            SQLTableSource right = table.getRight();
            SQLExpr onExpr = table.getCondition();
            if (left instanceof SQLSubqueryTableSource) {
                SQLSubqueryTableSource leftTable = ((SQLSubqueryTableSource) left);
                parseUnion(leftTable.getSelect().getQuery(), varMap);
                parseTable(leftTable.getSelect().getQueryBlock(), varMap);
            } else if (left instanceof SQLExprTableSource) {
                SQLExprTableSource joinTable = ((SQLExprTableSource) left);
                onExpr = createOnExpr(joinTable, onExpr, varMap);
            }
            if (right instanceof SQLSubqueryTableSource) {
                SQLSubqueryTableSource rightTable = ((SQLSubqueryTableSource) right);
                parseUnion(rightTable.getSelect().getQuery(), varMap);
                parseTable(rightTable.getSelect().getQueryBlock(), varMap);
            } else if (right instanceof SQLExprTableSource) {
                SQLExprTableSource joinTable = ((SQLExprTableSource) right);
                onExpr = createOnExpr(joinTable, onExpr, varMap);
            }
            table.setCondition(onExpr);
        }
    }

    /**
     * 如果是union all的情况,则通过递归进入内层
     * @param query
     * @param varMap
     * @return
     */
    private boolean parseUnion(final SQLSelectQuery query, final Map<String, String> varMap) {
        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery unionQuery = (SQLUnionQuery) query;
            if (unionQuery.getLeft() instanceof SQLUnionQuery) {
                parseUnion(unionQuery.getLeft(), varMap);
            } else if (unionQuery.getLeft() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) unionQuery.getLeft();
                parseTable(queryBlock, varMap);
            }
            if (unionQuery.getRight() instanceof SQLUnionQuery) {
                parseUnion(unionQuery.getRight(), varMap);
            } else if (unionQuery.getRight() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) unionQuery.getRight();
                parseTable(queryBlock, varMap);
            }
            return true;
        }
        return false;
    }

    /**
     * 在连接的on条件中拼接权限
     * @param joinTable join表名
     * @param onExpr 表达式
     * @param varMap 变量名
     * @return
     */
    //
    private SQLExpr createOnExpr(SQLExprTableSource joinTable, SQLExpr onExpr, final Map<String, String> varMap) {
        String tableName = joinTable.getName().getSimpleName();
        String aliasName = joinTable.getAlias();
        SQLExpr sqlExpr = createSQLExpr(tableName, aliasName, varMap);
        if (sqlExpr != null) {
            SQLBinaryOpExpr newWhereExpr = new SQLBinaryOpExpr(onExpr, SQLBinaryOperator.BooleanAnd, sqlExpr);
            onExpr = newWhereExpr;
        }
        return onExpr;
    }

    /**
     * 根据配置获取拼接好的权限SQL
     * @param tableName 表名
     * @param aliasName 别名
     * @param varMap 变量map
     * @return
     */
    private SQLExpr createSQLExpr(String tableName, String aliasName, final Map<String, String> varMap) {
        StringBuffer constraintsBuffer = new StringBuffer("");
        for (Privilege p : privList) {
            if (tableName.equals(p.getTableName())) {
                constraintsBuffer.append(" and ");
                constraintsBuffer.append(p.toString(aliasName, varMap));
            }
        }
        if ("".equals(constraintsBuffer.toString())) {
            return null;
        }
        SQLExprParser constraintsParser = SQLParserUtils
                .createExprParser(constraintsBuffer.toString().replaceFirst(" and ", ""), JdbcUtils.MYSQL);
        SQLExpr constraintsExpr = constraintsParser.expr();
        return constraintsExpr;
    }

    //拼接where中的权限信息
    private void createWhereSQLExpr(final SQLSelectQueryBlock query, final Map<String, String> varMap,
                                    SQLExpr sqlExpr) {
        if (sqlExpr == null) {
            return;
        }
        SQLExpr whereExpr = query.getWhere();
        // 修改where表达式
        if (whereExpr == null) {
            query.setWhere(sqlExpr);
        } else {
            SQLBinaryOpExpr newWhereExpr = new SQLBinaryOpExpr(whereExpr, SQLBinaryOperator.BooleanAnd, sqlExpr);
            query.setWhere(newWhereExpr);
        }
    }
}