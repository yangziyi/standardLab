package lab.dao;

import lab.po.userPO;
import lab.po.userPO2;
import lab.po.userUploadPO;
import lab.vo.userVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface uUserDao {
    List<userVO> findByUsername(userPO userPO);

    List<userVO> findByUsername2(userPO userPO);

    List<userVO> exportExcel(userPO userPO);

    void insertUser(userPO2 userPO2);

    //@param的意思是在Mapper文件里面的SQL参数名称以括号内的名字来代替
    void insertUsers(@Param("testUploadPO") List<userUploadPO> userUploadPO);

    void updUser(userPO2 userPO2);

    void updPwd(userPO2 userPO2);

    void delUser(userPO userPO);

    //查询主管id
    String findManageId(@Param("username") String username);
    //查询角色对应人员id
    List<String> findprocseeId(@Param("rolename") String rolename);

    //查询用户名是否重复
    Integer findUsername(@Param("username") String username, @Param("name") String name);
}
