package lab.base.util;

import com.alibaba.fastjson2.JSONObject;
import lab.entity.HttpResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP工具类
 */
public class HttpUtils {

	/**
	 * 获取HttpServletRequest对象
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 输出信息到浏览器
	 * @param response
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response, Object data) throws IOException {
		response.setContentType("application/json; charset=utf-8");
        String json = JSONObject.toJSONString(data);
        response.getWriter().print(json);
        response.getWriter().flush();
        response.getWriter().close();
	}
	
	/**
	 * 输出错误信息到浏览器
	 * @param response
	 * @throws IOException
	 */
	public static void errorWrite(HttpServletResponse response) throws IOException {
		response.setContentType("application/json; charset=utf-8");
        HttpResult result = HttpResult.getInstance();
        String json = JSONObject.toJSONString(result.error1());
        response.getWriter().print(json);
        response.getWriter().flush();
        response.getWriter().close();
	}
	
}