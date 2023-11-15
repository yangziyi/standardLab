package lab.dao;

import lab.po.controlsLogPO;
import lab.po.menuPO;
import lab.po.rolePO;
import lab.vo.menuVO;
import lab.vo.roleVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface menuDao {

    List<menuVO> findMenu(menuPO menuPO);

    void insRole(menuPO menuPO);

    void updRole(menuPO menuPO);

    void delRole(menuPO menuPO);
}
