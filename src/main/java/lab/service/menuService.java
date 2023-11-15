package lab.service;

import lab.dao.menuDao;
import lab.po.menuPO;
import lab.po.rolePO;
import lab.po.roleUserPO;
import lab.po.roleUserSelUpdPO;
import lab.vo.menuVO;
import lab.vo.roleVO;
import lab.vo.userVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class menuService {

    @Autowired
    private menuDao menuDao;

    public List<menuVO> findMenu(menuPO menuPO){
        return menuDao.findMenu(menuPO);
    }

    public void insMenu(menuPO menuPO){
        menuDao.insRole(menuPO);
    }

    public void updMenu(menuPO menuPO){
        menuDao.updRole(menuPO);
    }

    public void delMenu(menuPO menuPO){
        menuDao.delRole(menuPO);
    }
}
