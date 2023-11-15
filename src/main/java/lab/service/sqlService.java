package lab.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lab.base.util.HttpStatus;
import lab.base.util.RedisUtil;
import lab.base.util.SQLUtil;
import lab.dao.sqlDao;
import lab.entity.HttpResult;
import lab.entity.generalInfo;
import lab.po.*;
import lab.vo.outkeyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class sqlService {

    @Autowired
    private sqlDao sqlDao;

    @Autowired
    private RedisUtil RedisUtil;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public HttpResult createTable(createTablePO createTablePO) {

        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            String sql = SQLUtil.createTable(createTablePO);
            System.out.println(sql);
            //执行建表/视图语句
            sqlDao.executeSQL(sql);
        } catch (Exception e){
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("表单建立失败:" + e.toString());
            e.printStackTrace();
            return result;
        }

        try {
            //存储表单信息
            sqlDao.insT_table(createTablePO.getTablePO());
            int tableid = createTablePO.getTablePO().getId();
            String tabledocno = createTablePO.getTablePO().getTabledocno();
            sqlDao.insT_field(tableid, tabledocno, createTablePO.getFieldPOs());
        } catch (Exception e){
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("存储表单信息失败:" + e.toString());
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            sqlDao.dropTable(createTablePO.getTablePO().getTabledocno());
            e.printStackTrace();
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("表单" + createTablePO.getTablePO().getTabledocno() + "建立成功");
        return result;
    }

    public HttpResult selTable(tablePO tablePO){
        HttpResult result = HttpResult.getInstance();
        try {
            result.setPayload(sqlDao.selTable(tablePO));
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("查询失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("查询成功");
        return result;
    }

    public HttpResult selField(fieldPO fieldPO){
        HttpResult result = HttpResult.getInstance();
        try {
            result.setPayload(sqlDao.selField(fieldPO));
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("查询失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("查询成功");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public HttpResult updTable(updTablePO updTablePO) {

        HttpResult result = HttpResult.getInstance();
        try {
            //执行建表/视图语句
            sqlDao.executeSQL(updTablePO.getTablePO().getType());
            //修改表字段信息可能同时存在增删改一起操作的情况
            sqlDao.delT_table(updTablePO.getTablePO().getTabledocno());
            sqlDao.delT_field(updTablePO.getTablePO().getTabledocno());
            //存储表单信息
            int tableid = sqlDao.insT_table(updTablePO.getTablePO());
            String tabledocno = updTablePO.getTablePO().getTabledocno();
            sqlDao.insT_field(tableid, tabledocno, updTablePO.getFieldPOs());
        } catch (Exception e){
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("表单修改失败:" + e.toString());
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("表单" + updTablePO.getTablePO().getTabledocno() + "修改成功");
        return result;
    }

    public HttpResult delTable(delTablePO delTablePO) {
        HttpResult result = HttpResult.getInstance();
        try {
            //执行建表/视图语句
            sqlDao.dropTable(delTablePO.getTabledocno());
            //删除表和字段信息
            sqlDao.delT_table(delTablePO.getTabledocno());
            sqlDao.delT_field(delTablePO.getTabledocno());
        } catch (Exception e){
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("表单删除失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("表单" + delTablePO.getTabledocno() + "删除成功");
        return result;
    }

    public HttpResult insData(JSONObject json) {
        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            String sql = SQLUtil.insSplice(json);
            sqlDao.insData(sql);
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("数据新增失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("数据新增成功");
        return result;
    }

    public HttpResult updData(JSONObject json) {
        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            List<String> sqlList = SQLUtil.updSplice(json);
            sqlDao.updData(sqlList);
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("数据更新失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("数据更新成功");
        return result;
    }

    public HttpResult delData(JSONObject json) {
        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            List<String> sqlList = SQLUtil.delSplice(json);
            sqlDao.delData(sqlList);
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("数据删除失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("数据删除成功");
        return result;
    }

    public HttpResult selData(JSONObject json) {
        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            String sql = SQLUtil.selSplice(json);
            System.out.println(sql);
            result.setPayload(sqlDao.selData(sql));
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("数据查询失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("数据查询成功");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public HttpResult alterTables(@RequestBody alertTablePO alertTablePO) {

        HttpResult result = HttpResult.getInstance();
        if(!alertTablePO.getTcontroType().isEmpty()) {
            try {
                SQLUtil SQLUtil = new SQLUtil();
                String sql = "";
                if (alertTablePO.getTcontroType().equals("del")) {
                    sqlDao.dropTable(alertTablePO.getTabledocno());
                    sqlDao.delAlertT_table(alertTablePO);
                } else {
                    //updNewdocno
                    sql = SQLUtil.alterUpdTabledocno(alertTablePO);
                    sqlDao.updAlertT_table(alertTablePO);
                }
                System.out.println(sql);
                //执行建表/视图语句
                sqlDao.executeSQL(sql);
            } catch (Exception e) {
                if (result.getCode() == 0) {
                    result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
                result.setMsg("操作失败:" + e.toString());
                e.printStackTrace();
                return result;
            }
        }

        //判断是否存在需要修改的列
        if(alertTablePO.getAlertFieldPOs().size() > 0) {
            for (alertFieldPO v : alertTablePO.getAlertFieldPOs()) {
                try {
                    SQLUtil SQLUtil = new SQLUtil();
                    String sql = "";
                    if (v.getFcontroType().equals("add")) {
                        sql = SQLUtil.alterAdd(v);
                        //存储表单信息
                        sqlDao.insAlertT_field(v);
                    } else if (v.getFcontroType().equals("upd")) {
                        sql = SQLUtil.alterUpd(v);
                        sqlDao.updAlertT_field(v);
                    } else {
                        //del
                        sql = SQLUtil.alterDel(v);
                        sqlDao.delAlertT_field(v);
                    }
                    System.out.println(sql);
                    //执行建表/视图语句
                    sqlDao.executeSQL(sql);
                } catch (Exception e) {
                    // 手动回滚事务
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    if (result.getCode() == 0) {
                        result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                    }
                    result.setMsg("操作失败:" + e.toString());
                    e.printStackTrace();
                    return result;
                }
            }
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("操作成功");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public HttpResult alterTable(@RequestBody alertTablePO alertTablePO) {

        HttpResult result = HttpResult.getInstance();
        if(!alertTablePO.getTcontroType().isEmpty()) {
            try {
                SQLUtil SQLUtil = new SQLUtil();
                String sql = "";
                if (alertTablePO.getTcontroType().equals("del")) {
                    sqlDao.dropTable(alertTablePO.getTabledocno());
                    sqlDao.delAlertT_table(alertTablePO);
                } else {
                    if(alertTablePO.getNewTabledocno() != null && !alertTablePO.getNewTabledocno().isEmpty()) {
                        System.out.println("2145124124124124124:" + alertTablePO.getNewTabledocno());
                        //updNewdocno
                        sql = SQLUtil.alterUpdTabledocno(alertTablePO);
                        //执行建表/视图语句
                        sqlDao.executeSQL(sql);
                    }
                    sqlDao.updAlertT_table(alertTablePO);
                }
                System.out.println(sql);
            } catch (Exception e) {
                if (result.getCode() == 0) {
                    result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
                result.setMsg("操作失败:" + e.toString());
                e.printStackTrace();
                return result;
            }
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("操作成功");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public HttpResult alterField(@RequestBody alertFieldPO alertFieldPO) {

        HttpResult result = HttpResult.getInstance();
        try {
            SQLUtil SQLUtil = new SQLUtil();
            String sql = "";
            if (alertFieldPO.getFcontroType().equals("ins")) {
                sql = SQLUtil.alterAdd(alertFieldPO);
                //存储表单信息
                sqlDao.insAlertT_field(alertFieldPO);
            } else if (alertFieldPO.getFcontroType().equals("upd")) {
                sql = SQLUtil.alterUpd(alertFieldPO);
                sqlDao.updAlertT_field(alertFieldPO);
            } else {
                //del
                sql = SQLUtil.alterDel(alertFieldPO);
                sqlDao.delAlertT_field(alertFieldPO);
            }
            //执行建表/视图语句
            sqlDao.executeSQL(sql);
        } catch (Exception e) {
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (result.getCode() == 0) {
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("操作失败:" + e.toString());
            e.printStackTrace();
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("操作成功");
        return result;
    }

    public HttpResult setOutkey(outkeyPO outkeyPO) {
        HttpResult result = HttpResult.getInstance();
        try {

            if (outkeyPO.getFcontroType().equals("ins")) {
                sqlDao.insOutkey(outkeyPO);
            } else if (outkeyPO.getFcontroType().equals("upd")) {
                sqlDao.updOutkey(outkeyPO);
            } else {
                //del
                sqlDao.delOutkey(outkeyPO);
            }
            //重置redis缓存
            redisOutkey();
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("操作失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("操作成功");
        return result;
    }

    public HttpResult selOutkey(outkeyPO outkeyPO){
        HttpResult result = HttpResult.getInstance();
        try {
            result.setPayload(sqlDao.selOutkey(outkeyPO));
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("查询失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("查询成功");
        return result;
    }

    /**
     * redis的外键缓存
     * 为了方便重启清空重置缓存等问题，存get就好
     * redis格式设计:{table2:{table1:{"table2.table1id = table1.id and table2.table1docno = table1.docno"}}}
     */
    public void redisOutkey(){
        List<outkeyVO> list = sqlDao.selOutkey(new outkeyPO());
        Map<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        list.forEach( s -> {
            String[] fields = s.getField().split(",");
            String[] out_fields = s.getOutField().split(",");
            HashMap<String, String> lmap = new HashMap<String, String>();
            String where = " ";
            if(fields.length > 0){
                for(int i = 0; i < fields.length; i ++){
                    if(i > 0){
                        where += " and ";
                    }
                    where += s.getTabledocno() + "." + fields[i]
                            + " = " + s.getOutTabledocno() + "." +out_fields[i];
                    lmap.put(s.getOutTabledocno(), where);
                }
                //RedisUtil.hset(s.getTabledocno(), s.getOut_tabledocno(), where);
                map.put(s.getTabledocno(), lmap);
            }
        });
        RedisUtil.set("outKey", map);
    }

    public HttpResult executeSQL(String sql){
        HttpResult result = HttpResult.getInstance();
        try {
            if(sql.startsWith("select")){
                result.setPayload(sqlDao.selData(sql));
            } else {
                sqlDao.executeSQL(sql);
            }
        } catch (Exception e){
            e.printStackTrace();
            if(result.getCode() == 0){
                result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            result.setMsg("执行失败:" + e.toString());
            return result;
        }

        if(result.getCode() == 0){
            result.setCode(HttpStatus.SC_OK);
        }
        result.setMsg("执行成功");
        return result;
    }
}
