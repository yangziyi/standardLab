package lab.base.util.aspect;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lab.base.util.JwtTokenUtils;
import lab.dao.userDao;
import lab.entity.User;
import lab.entity.userInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @description： 日志切面 打印请求日志
 */
@Slf4j   // lombok中日志注解
@Aspect  // 表明是一个切面类
@Component
public class controlsAspect {

    @Autowired
    private userDao userDao;

    public controlsAspect() {
    }

    /**
     * 定义切入点表达式
     * 访问修饰符 返回值 包名.包名.包名...类名.方法名(参数列表)
     * 权限修饰符可以使用默认 第一个*表示返回值类型  ..表示当前包以及其子包下 第二个*表示任意方法 (..)表示任意参数列表
     */
    private final String POINTCUT = "execution(* lab.controller..*(..))";
    private final String INSPOINTCUT = "execution(* lab.controller..ins*(..))";
    @Pointcut("execution(* lab.controller..ins*(..))")
    public void insControlsLogInfo() {
    }
    private final String UPDPOINTCUT = "execution(* lab.controller..upd*(..))";
    @Pointcut("execution(* lab.controller..upd*(..))")
    public void updControlsLogInfo() {
    }

    @Before(POINTCUT)
    public void doBefore(JoinPoint joinPoint) {
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        userInfo userInfos = userInfo.getInstance();
        String token = JwtTokenUtils.getToken(request);
        String username = JwtTokenUtils.getUsernameFromToken(token);
        User user = userDao.findByUsername(username);

        userInfos.setOwnerid(user.getId());
        userInfos.setOwnerusername(user.getUsername());
        userInfos.setOwnername(user.getName());
        userInfos.setUpdateid(user.getId());
        userInfos.setUpdateusername(user.getUsername());
        userInfos.setUpdatename(user.getName());

    }

}