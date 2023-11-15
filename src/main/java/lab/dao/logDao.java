package lab.dao;

import lab.po.controlsLogPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface logDao {

    void insT_controls_log(controlsLogPO controlsLogPO);
}
