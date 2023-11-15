package lab.controller;

import com.alibaba.fastjson2.JSONObject;
import lab.base.util.aspect.serviceAround;
import lab.entity.HttpResult;
import lab.po.*;
import lab.service.sqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sql")
public class sqlController {
    @Autowired
    private sqlService sqlService;

    @serviceAround
    @RequestMapping(value = "/createTable" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult createTable(@RequestBody createTablePO createTablePO) {
        return sqlService.createTable(createTablePO);
    }

    @serviceAround
    @RequestMapping(value = "/updTable" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult updTable(@RequestBody updTablePO updTablePO) {
        return sqlService.updTable(updTablePO);
    }

    @serviceAround
    @RequestMapping(value = "/delTable" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult delTable(@RequestBody delTablePO delTablePO) {
        return sqlService.delTable(delTablePO);
    }

    @serviceAround
    @RequestMapping(value = "/insData" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult insData(@RequestBody JSONObject json) {
        return sqlService.insData(json);
    }

    @serviceAround
    @RequestMapping(value = "/updData" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult updData(@RequestBody JSONObject json) {
        return sqlService.updData(json);
    }

    @serviceAround
    @RequestMapping(value = "/delData" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult delData(@RequestBody JSONObject json) {
        return sqlService.delData(json);
    }

    @RequestMapping(value = "/selData" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult selData(@RequestBody JSONObject json) {
        return sqlService.selData(json);
    }

    @serviceAround
    @RequestMapping(value = "/alterTables" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult alterTables(@RequestBody alertTablePO alertTablePO) {
        return sqlService.alterTables(alertTablePO);
    }

    @serviceAround
    @RequestMapping(value = "/alterTable" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult alterTable(@RequestBody alertTablePO alertTablePO) {
        return sqlService.alterTable(alertTablePO);
    }

    @serviceAround
    @RequestMapping(value = "/alterField" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult alterField(@RequestBody alertFieldPO alertFieldPO) {
        return sqlService.alterField(alertFieldPO);
    }

    @RequestMapping(value = "/selTable" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult selTable(@RequestBody tablePO tablePO){
        return sqlService.selTable(tablePO);
    }

    @RequestMapping(value = "/selField" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult selField(@RequestBody fieldPO fieldPO){
        return sqlService.selField(fieldPO);
    }

    @serviceAround
    @RequestMapping(value = "/setOutkey" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult setOutkey(@RequestBody outkeyPO outkeyPO){
        return sqlService.setOutkey(outkeyPO);
    }

    @RequestMapping(value = "/selOutkey" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult selOutkey(@RequestBody outkeyPO outkeyPO){
        return sqlService.selOutkey(outkeyPO);
    }

    @RequestMapping(value = "/executeSQL" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult executeSQL(@RequestBody Map<String, String> map){
        return sqlService.executeSQL(map.get("sql"));
    }

}
