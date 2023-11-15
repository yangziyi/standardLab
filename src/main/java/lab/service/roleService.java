package lab.service;

import lab.po.rolePO;
import lab.po.roleUserPO;
import lab.po.roleUserSelUpdPO;
import lab.vo.roleVO;
import lab.vo.userVO;
import lab.dao.roleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class roleService {

    @Autowired
    private roleDao roleDao;

    public Integer findRoleCount(rolePO rolePO){
        return roleDao.findRoleCount(rolePO);
    }

    public List<roleVO> findRole(rolePO rolePO){
        return roleDao.findRole(rolePO);
    }

    public void insRole(rolePO rolePO){
        roleDao.insertRole(rolePO);
    }

    public void updRole(rolePO rolePO){
        roleDao.updRole(rolePO);
    }

    public void delRole(rolePO rolePO){
        roleDao.delRole(rolePO);
    }

    public List<userVO> findRoleUsers(roleUserSelUpdPO roleUserSelUpdPO){
        return roleDao.findRoleUsers(roleUserSelUpdPO);
    }

    public Integer findRoleUserCount(roleUserSelUpdPO roleUserSelUpdPO){
        return roleDao.findRoleUserCount(roleUserSelUpdPO);
    }

    public List<userVO> findNoRoleUsers(roleUserSelUpdPO roleUserSelUpdPO){
        return roleDao.findNoRoleUsers(roleUserSelUpdPO);
    }
    public void insRoleUsers(roleUserPO roleUserPO){
        roleDao.insertRoleUsers(roleUserPO);
    }

    public void delRoleUsers(roleUserSelUpdPO roleUserSelUpdPO){
        roleDao.delRoleUsers(roleUserSelUpdPO);
    }
}
