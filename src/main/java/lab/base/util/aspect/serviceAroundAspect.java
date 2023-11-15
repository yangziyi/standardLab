package lab.base.util.aspect;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lab.base.util.HttpStatus;
import lab.dao.logDao;
import lab.entity.HttpResult;
import lab.entity.userInfo;
import lab.po.controlsLogPO;
import lab.base.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 重复提交aop
 */
@Aspect
@Component
public class serviceAroundAspect {

    @Autowired
    private RedisUtil RedisUtil;
    @Autowired
    private logDao logDao;

    /**
     * @param point
     */
    @Lazy
    @Around("@annotation(lab.base.util.aspect.serviceAround)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //获取请求参数
        Object[] args = point.getArgs();
        //获取请求头
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        //目标类、方法
        String className = method.getDeclaringClass().getName();
        String process = method.getName();

        //防止重复提交-5秒内同一IP不得重复调用同一接口
        String ip = request.getRemoteAddr();
        String ipKey = String.format("%s#%s", className, process);
        int hashCode = Math.abs(ipKey.hashCode());
        String key = String.format("%s_%d", ip, hashCode);
        serviceAround serviceAround =  method.getAnnotation(serviceAround.class);
        String value = (String) RedisUtil.get(key);
        if (StringUtils.isNotBlank(value)){
            HttpResult result = HttpResult.getInstance();
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setMsg("请勿频繁重复操作");
            return result;
        }
        RedisUtil.set(key, UUID.randomUUID().toString(), 5);

        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(args[0]));
        userInfo userInfos = userInfo.getInstance();
        if(process.startsWith("ins")){
            json.put("ownerid", userInfos.getOwnerid());
            json.put("ownerusername", userInfos.getOwnerusername());
            json.put("ownername", userInfos.getOwnername());
        }
        json.put("updateid", userInfos.getOwnerid());
        json.put("updateusername", userInfos.getOwnerusername());
        json.put("updatename", userInfos.getOwnername());
        args[0] = JSONObject.parseObject(JSONObject.toJSONString(json), args[0].getClass());

        //执行方法
        Object object = point.proceed(args);

        //方法执行完之后
        //将用户的操作及相关数据存入数据库
        try{
            String controls = "";
            if(process.startsWith("ins")){
                controls = "insert";
            } else if (process.startsWith("upd")) {
                controls = "update";
            } else if (process.startsWith("del")) {
                controls = "delete";
            } else {
                controls = process;
            }
            controlsLogPO controlsLogPO = new controlsLogPO();
            controlsLogPO.setClassname(className);
            controlsLogPO.setProcess(process);
            controlsLogPO.setControls(controls);
            controlsLogPO.setContent(JSONObject.toJSONString(args[0]));
            controlsLogPO.setResult(object.toString());
            controlsLogPO.setOwnerid(userInfos.getOwnerid());
            controlsLogPO.setOwnerusername(userInfos.getOwnerusername());
            controlsLogPO.setOwnername(userInfos.getOwnername());
            controlsLogPO.setUpdateid(userInfos.getOwnerid());
            controlsLogPO.setUpdateusername(userInfos.getOwnerusername());
            controlsLogPO.setUpdatename(userInfos.getOwnername());
            logDao.insT_controls_log(controlsLogPO);
        } catch(Exception e){
            e.printStackTrace();
        }
        return object;
    }

}
