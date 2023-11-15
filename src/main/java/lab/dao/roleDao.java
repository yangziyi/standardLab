package lab.dao;

import lab.po.rolePO;
import lab.po.roleUserPO;
import lab.po.roleUserSelUpdPO;
import lab.vo.roleVO;
import lab.vo.userVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface roleDao {
    Integer findRoleCount(rolePO rolePO);

    List<roleVO> findRole(rolePO rolePO);

    void insertRole(rolePO rolePO);

    void updRole(rolePO rolePO);

    void delRole(rolePO rolePO);

    List<userVO> findRoleUsers(roleUserSelUpdPO roleUserSelUpdPO);

    Integer findRoleUserCount(roleUserSelUpdPO roleUserSelUpdPO);

    List<userVO> findNoRoleUsers(roleUserSelUpdPO roleUserSelUpdPO);

    void insertRoleUsers(roleUserPO roleUserPO);

    void delRoleUsers(roleUserSelUpdPO roleUserSelUpdPO);
}
