package lab.base.config.security;

import lab.base.util.HttpStatus;
import lab.dao.userDao;
import lab.entity.HttpResult;
import lab.entity.User;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户登录认证信息查询
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private userDao UserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpResult result = HttpResult.getInstance();
        //每次进来都把响应结果值初始化
        result.setCode(HttpStatus.SC_OK);
        result.setMsg(null);

        User user = UserDao.findByUsername(username);
        if (user == null) {
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setMsg("该用户不存在");
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        Set<String> permissions = UserDao.findPermissions(username);
        if (permissions.size() == 0) {
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setMsg("该用户未配置任何权限，请先在后台配置后再登入");
            throw new PersistenceException("该用户未配置任何权限，请先在后台配置后再登入");
        }
        
        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        
        return new JwtUserDetails(username, user.getPassword(), grantedAuthorities);
    }
}