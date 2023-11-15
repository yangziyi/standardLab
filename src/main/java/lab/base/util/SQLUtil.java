package lab.base.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lab.entity.userInfo;
import lab.po.alertFieldPO;
import lab.po.alertTablePO;
import lab.po.createTablePO;
import lab.po.fieldPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.RequestContextFilter;

import java.util.*;

@Component
public class SQLUtil {

    @Autowired
    private RedisUtil RedisUtil;

    /**
     * 拼接插入数据的sql
     * @param json
     * @return
     */
    public String insSplice(JSONObject json){
        String sql = "";
        userInfo userInfos = userInfo.getInstance();
        //table:[ table1,table2,table3]
        JSONArray table = json.getJSONArray("table");
        for(Object s : table){
            JSONArray tablex = json.getJSONArray(s.toString());
            sql += "insert into " + s.toString() + " ";
            String key = "(";
            for (int i = 0; i < tablex.size(); i ++){
                String value = "(";
                JSONObject fieldx = tablex.getJSONObject(i);
                //处理拼接key与value
                for (String k : fieldx.keySet()){
                    key += k;
                    key += ",";
                    if(fieldx.get(k) instanceof String) {
                        value += "'" + fieldx.getString(k) + "'";
                    } else if (fieldx.get(k) instanceof Integer) {
                        value += fieldx.getString(k);
                    }
                    value += ",";
                }
                if (i == 0) {
                    //加入数据操作者基本信息
                    key += " ownerid, ownerusername, ownername, createtime, updateid, updateusername, updatename, updatetime)";
                    //拼接key
                    sql += key;
                    sql += " values ";
                }
                value +=  userInfos.getOwnerid() + ",";
                value += "'" + userInfos.getOwnerusername() +"',";
                value += "'" + userInfos.getOwnername() +"',";
                value += "now(),";
                value += userInfos.getOwnerid() + ",";
                value += "'" + userInfos.getOwnerusername() +"',";
                value += "'" + userInfos.getOwnername() +"',";
                value += "now())";
                if(i < tablex.size() - 1){
                    value += ",";
                }
                //拼接value
                sql += value;
            }
            sql += ";";
        }

        return sql;
    }

    /**
     * 拼接插入sql
     * @param json
     * @return
     */
    public List<String> updSplice(JSONObject json){
        List<String> sqlList = new ArrayList<>();
        userInfo userInfos = userInfo.getInstance();
        //table:[ table1,table2,table3]
        JSONArray table = json.getJSONArray("table");
        for(Object s : table){
            JSONArray tablex = json.getJSONArray(s.toString());
            for (int i = 0; i < tablex.size(); i ++){
                String sql = "update " + s.toString() + " set ";
                String where = "";
                JSONObject fieldx = tablex.getJSONObject(i);
                int j = 0;
                for (String k : fieldx.keySet()){
                    if(k.equals("id")){
                        where = " where " + k + " = " + fieldx.getString(k);
                        continue;
                    }

                    if(fieldx.get(k) instanceof String) {
                        sql += k + " = '" + fieldx.getString(k) + "'";
                    } else if (fieldx.get(k) instanceof Integer) {
                        sql += k + "=" + fieldx.getString(k);
                    }
                    sql += ",";
                }
                sql += "updateid = " + userInfos.getOwnerid() + ",updateusername = " + userInfos.getOwnerid() +
                        ",updatename = " + userInfos.getOwnerid() + ",updatetime = now()";
                //更新一定要有条件，不然整张表都会更新
                if(where != null && !where.isEmpty()){
                    sql = sql.concat(where);
                    sqlList.add(sql);
                }
            }
        }

        return sqlList;
    }

    public List<String> delSplice(JSONObject json){
        List<String> sqlList = new ArrayList<>();
        //table:[ table1,table2,table3]
        JSONArray table = json.getJSONArray("table");
        for(Object s : table){
            JSONArray tablex = json.getJSONArray(s.toString());
            for (int i = 0; i < tablex.size(); i ++){
                String sql = "delete from " + s.toString();
                String where = "";
                JSONObject fieldx = tablex.getJSONObject(i);
                for (String k : fieldx.keySet()){
                    where = " where " + k + " = " + fieldx.getString(k);
                }
                sql = sql.concat(where);
                sqlList.add(sql);
            }
        }

        return sqlList;
    }

    /**
     * {
     * "table":["table1","table2","table3"],
     * "field":"{table1:[List<field>],table2:[List<field>],table3:[List<field>]},与table对应,不同表的field不能同名不然json会吞掉其他的
     * "basicInfo":"Y",是否需要所有人创建时间最后修改时间等基础信息，Y-是,N否
     * "primaryTable":"table1",确定主表
     * "join":{table2:table2.id=table1.id and table2.docno=table1.docno,table3:table3.id=table1.id},关联表与主表的join字符串
     * "where":"{"table1.id=1 and table1.docno=123",where条件字符串
     * "sum":["id","docno"],汇总字段，后台程序自动给字段加上sum,如果没有则不要添加此信息
     * "count":["id","docno"],统计字段，后台程序自动给字段加上sum,同上字段一起,如果没有则不要添加此信息
     * "orderby":["id","createtime"]
     * }
     * @param json
     * @return
     */
    public String selSplice(JSONObject json){
        Object data = new Object();                                                 //返回值
        String sql = "";
        String fields = fields(json);                                               //SQL所需字段集合
        //String primaryTable = json.getString("primaryTable");   //主表
        String primaryTable = json.getJSONArray("table").getString(0);    //约定好第一张就是主表
        String join = join(json);                                                   //所有关联表的关联语句
        String where = json.getString("where");                                 //条件
        String groupby = groupby(json, fields);                                     //聚合字段
        String orderby = orderby(json);                                             //排序字段

        //拼接
        sql = "select " + fields
                + " from " + primaryTable
                + join
                + " where " + where
                + groupby
                + orderby;

        return sql;
    }

    /**
     * 整理所有需要查询的字段
     * @param json
     * @return
     */
    public String fields(JSONObject json){
        String fields = "";
        //String primaryTable = json.getString("primaryTable");   //主表
        String primaryTable = json.getJSONArray("table").getString(0);    //约定好第一张就是主表
        String basicInfo = json.getString("basicInfo");         //是否需要基础信息
        JSONArray sum = json.getJSONArray("sum");               //汇总字段数组用于判断字段是否存在于此
        JSONArray count = json.getJSONArray("count");           //统计字段数组用于判断字段是否存在于此
        //先解析有多少张表
        JSONArray table = json.getJSONArray("table");
        for(int i = 0; i < table.size(); i ++){
            //获取field:{table1:[List<field>],table2:[List<field>],table3:[List<field>]}里的各table数组
            JSONArray tableFields = json.getJSONObject("field").getJSONArray(table.getString(i));
            if(tableFields != null) {
                for (int j = 0; j < tableFields.size(); j++) {
                    String field = tableFields.getString(j);
                    if (sum != null && sum.indexOf(field) > -1) {                //是否汇总字段
                        fields += "sum(" + table.getString(i) + "." + field + ") " + field + ",";
                    } else if (count != null && count.indexOf(field) > -1) {     //是否统计字段
                        fields += "count(" + table.getString(i) + "." + field + ") " + field + ",";
                    } else {                                    //常规查询字段
                        fields += table.getString(i) + "." + field + ",";
                    }
                }
            }
        }
        if(basicInfo != null && basicInfo.equals("Y")) {
            //表单基础信息ownerid, ownerusername, ownername, createtime, updateid, updateusername, updatename, updatetime
            fields += primaryTable + "." + "ownerid,"
                    + primaryTable + "." + "ownerusername,"
                    + primaryTable + "." + "ownername,"
                    + primaryTable + "." + "createtime,"
                    + primaryTable + "." + "updateid,"
                    + primaryTable + "." + "updateusername,"
                    + primaryTable + "." + "updatename,"
                    + primaryTable + "." + "updatetime ";
        } else {
            //不需要则去掉最后一位的逗号
            fields = fields.substring(0, fields.length() - 1);
        }

        return fields;
    }

    /**
     * RedisUtil.set("outKey", map);
     * map格式设计:{table2:{table1:{"table2.table1id = table1.id and table2.table1docno = table1.docno"}}}
     * 拼接join字符串
     * @param json
     * @return
     */
    public String join(JSONObject json){
        if (RedisUtil == null) {
            HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            RedisUtil = wac.getBean(RedisUtil.class);
        }
        Map<String, HashMap<String, String>> map = (Map<String, HashMap<String, String>>) RedisUtil.get("outKey");
        String join = "";
        JSONArray tables = json.getJSONArray("table");
        //数组长度大于1说明有关联表
        if(tables.size() > 1){
            //与前端约定第一个表为主表可以省略系统一次计算
            for(int i = 1; i < tables.size(); i ++){
                String table = tables.getString(i);
                for(int j = i - 1; j > -1; j --) {
                    String table2 = tables.getString(j);
                    String where = map.get(table).get(table2);
                    if(where != null && !where.isEmpty()) {
                        join += " left join " + table + " on " + where;
                    }
                }
            }

        }
        /*if(json.containsKey("join")) {
            JSONObject joins = json.getJSONObject("join");
            for (String k : joins.keySet()) {
                join += " left join " + k + " on " + joins.getString(k);
            }
        }*/

        return join;
    }

    /**
     * 整理group by字段sql
     * @param json
     * @return
     */
    public String groupby(JSONObject json, String fields){
        String groupby = "";
        JSONArray sum = json.getJSONArray("sum");               //汇总字段数组用于判断字段是否存在于此
        JSONArray count = json.getJSONArray("count");           //统计字段数组用于判断字段是否存在于此
        if((sum !=null && sum.size() > 0) || (count != null && count.size() > 0)) {
            String[] str = fields.split(",");
            for (String s : str) {
                if (s.toString().startsWith("sum(") || s.toString().startsWith("count(")) {
                    continue;
                }
                groupby += s.toString();
                groupby += ",";
            }

            if (groupby.length() > 0) {
                groupby = groupby.substring(0, groupby.length() - 1);
                groupby = " group by " + groupby;
            }
        }

        /*if(json.containsKey("sum") || json.containsKey("count")) {
            Set<Object> set = new HashSet<>(Arrays.asList(fields.split(",")));
            Set<Object> sumSet = new HashSet<>();
            Set<Object> groupSet = new HashSet<>();
            if(json.containsKey("sum")) {
                sumSet.addAll(json.getJSONArray("sum")); //汇总字段数组用于判断字段是否存在于此
            }
            if(json.containsKey("count")) {
                groupSet.addAll(json.getJSONArray("count")); //汇总字段数组用于判断字段是否存在于此
            }

            developUtil developUtil = new developUtil();
            Set<Object> reSet = developUtil.differenceSet(set, sumSet);
            reSet = developUtil.differenceSet(set, groupSet);

            if(reSet.size() > 0){
                groupby = " group by ";
                for (Object s : reSet) {
                    if(s.toString().startsWith("sum(") || s.toString().startsWith("count(")){
                        continue;
                    }
                    groupby += s.toString();
                    groupby += ",";
                }
                groupby = groupby.substring(0, groupby.length() - 1);
            }
        }*/

        return groupby;
    }

    public String orderby(JSONObject json){
        String orderby = "";

        if(json.containsKey("orderby")) {
            JSONArray orderbys = json.getJSONArray("orderby");
            //排序字段数组用于判断字段是否存在于此
            if(orderbys.size() > 0) {
                orderby = " order by ";
                for (int i = 0; i < orderbys.size(); i++) {
                    if(i > 0){
                        orderby += ",";
                    }
                    orderby += orderbys.getString(i);
                }
            }
        }

        return orderby;
    }


    /**
     * 由于主键等拼接信息写在全局参数不合适，又要各种判断编辑，所以这个方法不好拆分开
     * "sql":"CREATE TABLE IF NOT EXISTS `t_test`(`id` INT UNSIGNED AUTO_INCREMENT,`title` VARCHAR(100) NOT NULL," +
     *         "`author` VARCHAR(40) NOT NULL,`date` DATE," +
     *         "PRIMARY KEY ( `id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;"
     * @param createTablePO
     * @return
     */
    public String createTable(createTablePO createTablePO) {
        String sql = "";
        //建表语句的开头
        String createTableStart = "CREATE TABLE IF NOT EXISTS ";
        //建表语句的表名
        String tabledocno = createTablePO.getTablePO().getTabledocno();
        //建表语句的字段主体
        String body = "";
        //建表语句的主键
        String primaryKey = "";
        //建表语句的结尾
        String createTableEnd = "ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        for (int i = 0; i < createTablePO.getFieldPOs().size(); i++) {
            if(i > 0){
                body += ",";
            }
            fieldPO v = createTablePO.getFieldPOs().get(i);

            //字段名、类型
            body += v.getFielddocno();
            String type = v.getType();
            if(type.equals("int") || type.equals("INT")
                    || type.equals("smallint") || type.equals("INT")
                    || type.equals("bigint") || type.equals("BIGINT")){
                //类型、长度
                if(v.getLength() != null){
                    body += "\t";
                    body += type + "(" + v.getLength() + ")";
                } else {
                    body += "\t";
                    body += type;
                }
                //自增
                if(v.getIncrement().equals("Y")){
                    body += "\t";
                    body += "AUTO_INCREMENT";
                }
            } else if (type.equals("double") || type.equals("DOUBLE")
                    || type.equals("float") || type.equals("FLOAT")) {
                //类型、长度、小数位
                body += "\t";
                body += type + "(" + v.getLength() + "," + v.getDecimals() + ")";
            } else if (type.equals("varchar") || type.equals("VARCHAR")) {
                //类型、长度
                body += "\t";
                body += type + "(" + v.getLength() + ")";
            } else {
                //其他类型
                body += "\t";
                body += type;
            }

            //是否为主键
            if(v.getPrimaryKey().equals("Y")){
                body += "\t";
                body += "PRIMARY KEY";
            }
            //非空
            if (v.getNotNull().equals("Y")){
                body += "\t";
                body += "NOT NULL";
            }
            //默认值
            if (v.getDefaults() != null && !v.getDefaults().isEmpty()){
                body += "\t";
                body += "default " + "'" + v.getDefaults() + "'";
            }
        }   //本循环到这里截止

        //添加本项目数据库构建字段
        body += "," + generalField();

        body += "\t";

        sql = createTableStart + tabledocno + "(" + body + ")" + createTableEnd;

        return sql;
    }

    /**
     * 返回本项目各表的构建参数
     * @return
     */
    public String generalField() {
        String str = "ownerid int,ownerusername varchar(255),ownername varchar(255),updateid int,createtime datetime," +
                "updateusername varchar(255),updatename varchar(255),updatetime datetime";
        return str;
    }

    /**
     * ALTER TABLE table_name ADD column_name data_type;
     * @param alertFieldPO
     * @return
     */
    public String alterAdd(alertFieldPO alertFieldPO) {
        String sql = "";
        //alter开头
        String alterStart = "ALTER TABLE ";
        //表名
        String tablename = alertFieldPO.getTabledocno();
        //字段名
        String fieldname = alertFieldPO.getFielddocno();
        //字段类型varchar、int等
        String datatype = "";

        String type = alertFieldPO.getType();
        if(type.equals("int") || type.equals("INT")
                || type.equals("smallint") || type.equals("INT")
                || type.equals("bigint") || type.equals("BIGINT")){
            //类型、长度
            if(alertFieldPO.getLength() != null){
                datatype += "\t";
                datatype += type + "(" + alertFieldPO.getLength() + ")";
            } else {
                datatype += "\t";
                datatype += type;
            }
            //自增
            if(alertFieldPO.getIncrement().equals("Y")){
                datatype += "\t";
                datatype += "AUTO_INCREMENT";
            }
        } else if (type.equals("double") || type.equals("DOUBLE")
                || type.equals("float") || type.equals("FLOAT")) {
            //类型、长度、小数位
            datatype += "\t";
            datatype += type + "(" + alertFieldPO.getLength() + "," + alertFieldPO.getDecimals() + ")";
        } else if (type.equals("varchar") || type.equals("VARCHAR")) {
            //类型、长度
            datatype += "\t";
            datatype += type + "(" + alertFieldPO.getLength() + ")";
        } else {
            //其他类型
            datatype += "\t";
            datatype += type;
        }

        //是否为主键
        if(alertFieldPO.getPrimaryKey().equals("Y")){
            datatype += "\t";
            datatype += "PRIMARY KEY";
        }
        //非空
        if (alertFieldPO.getNotNull().equals("Y")){
            datatype += "\t";
            datatype += "NOT NULL";
        }
        //默认值
        if (alertFieldPO.getDefaults() != null && !alertFieldPO.getDefaults().isEmpty()){
            datatype += "\t";
            datatype += "default " + "'" + alertFieldPO.getDefaults() + "'";
        }

        sql = alterStart + tablename + " ADD " + fieldname + datatype;

        return sql;
    }

    /**
     * ALTER TABLE table_name MODIFY column_name new_data_type;
     * ALTER TABLE table_name CHANGE old_column_name new_column_name data_type;
     * @param alertFieldPO
     * @return
     */
    public String alterUpd(alertFieldPO alertFieldPO) {
        String sql = "";
        //alter开头
        String alterStart = "ALTER TABLE ";
        //表名
        String tablename = alertFieldPO.getTabledocno();
        //字段名
        String fieldname = alertFieldPO.getFielddocno();
        //字段类型varchar、int等
        String datatype = "";

        String type = alertFieldPO.getType();
        if(type.equals("int") || type.equals("INT")
                || type.equals("smallint") || type.equals("INT")
                || type.equals("bigint") || type.equals("BIGINT")){
            //类型、长度
            if(alertFieldPO.getLength() != null){
                datatype += "\t";
                datatype += type + "(" + alertFieldPO.getLength() + ")";
            } else {
                datatype += "\t";
                datatype += type;
            }
            //自增
            if(alertFieldPO.getIncrement().equals("Y")){
                datatype += "\t";
                datatype += "AUTO_INCREMENT";
            }
        } else if (type.equals("double") || type.equals("DOUBLE")
                || type.equals("float") || type.equals("FLOAT")) {
            //类型、长度、小数位
            datatype += "\t";
            datatype += type + "(" + alertFieldPO.getLength() + "," + alertFieldPO.getDecimals() + ")";
        } else if (type.equals("varchar") || type.equals("VARCHAR")) {
            //类型、长度
            datatype += "\t";
            datatype += type + "(" + alertFieldPO.getLength() + ")";
        } else {
            //其他类型
            datatype += "\t";
            datatype += type;
        }

        //是否为主键
        if(alertFieldPO.getPrimaryKey().equals("Y")){
            datatype += "\t";
            datatype += "PRIMARY KEY";
        }
        //非空
        if (alertFieldPO.getNotNull().equals("Y")){
            datatype += "\t";
            datatype += "NOT NULL";
        }
        //默认值
        if (alertFieldPO.getDefaults() != null && !alertFieldPO.getDefaults().isEmpty()){
            datatype += "\t";
            datatype += "default " + "'" + alertFieldPO.getDefaults() + "'";
        }

        if(alertFieldPO.getNewFielddocno() != null && !alertFieldPO.getNewFielddocno().isEmpty()) {
            sql = alterStart + tablename + " CHANGE " + fieldname + " " + alertFieldPO.getNewFielddocno() + datatype;
        } else {
            sql = alterStart + tablename + " MODIFY " + fieldname + datatype;
        }

        return sql;
    }

    /**
     * ALTER TABLE table_name DROP column_name;
     * @param alertFieldPO
     * @return
     */
    public String alterDel(alertFieldPO alertFieldPO) {
        String sql = "";
        //alter开头
        String alterStart = "ALTER TABLE ";
        //表名
        String tablename = alertFieldPO.getTabledocno();
        //字段名
        String fieldname = alertFieldPO.getFielddocno();

        sql = alterStart + tablename + " DROP " + fieldname;

        return sql;
    }

    /**
     * ALTER TABLE old_table_name RENAME TO new_table_name;
     * @param alertTablePO
     * @return
     */
    public String alterUpdTabledocno(alertTablePO alertTablePO) {
        String sql = "";
        //alter开头
        String alterStart = "ALTER TABLE ";
        //表名
        String tablename = alertTablePO.getTabledocno();
        //字段名
        String newTablename = alertTablePO.getNewTabledocno();

        sql = alterStart + tablename + " RENAME TO " + newTablename;

        return sql;
    }
}
