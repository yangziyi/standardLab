package lab.dao;

import lab.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface userDao {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findByUsername(@Param("username") String username);

    /**
     * 查找用户的菜单权限标识集合
     * @param username
     * @return
     */
    Set<String> findPermissions(@Param("username") String username);
}
