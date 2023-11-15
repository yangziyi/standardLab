package lab.dao;

import lab.po.*;
import lab.vo.fieldVO;
import lab.vo.outkeyVO;
import lab.vo.roleVO;
import lab.vo.tableVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface sqlDao {

    void executeSQL(@Param("sql") String sql);

    void dropTable(@Param("tabledocno") String tabledocno);

    void insData(@Param("sql") String sql);

    void updData(@Param("list") List<String> sqlList);

    void delData(@Param("list") List<String> sqlList);

    List<LinkedHashMap<String, Object>> selData(@Param("sql") String sql);

    List<tableVO> selTable(tablePO tablePO);

    int insT_table(tablePO tablePO);

    List<fieldVO> selField(fieldPO fieldPO);

    void insT_field(@Param("tableid") int tableid, @Param("tabledocno") String tabledocno, @Param("fieldPO") List<fieldPO> fieldPO);

    void insAlertT_field(alertFieldPO alertFieldPO);

    void updT_table(tablePO tablePO);

    void updAlertT_table(alertTablePO alertTablePO);

    void updT_field(@Param("fieldPO") List<fieldPO> fieldPO);

    void updAlertT_field(alertFieldPO alertFieldPO);

    List<roleVO> findRole(rolePO rolePO);

    void delT_table(@Param("tabledocno") String tabledocno);

    void delAlertT_table(alertTablePO alertTablePO);

    void delT_field(@Param("tabledocno") String tabledocno);

    void delAlertT_field(alertFieldPO alertFieldPO);

    void insOutkey(outkeyPO outkeyPO);

    void delOutkey(outkeyPO outkeyPO);

    void updOutkey(outkeyPO outkeyPO);

    List<outkeyVO> selOutkey(outkeyPO outkeyPO);
}
