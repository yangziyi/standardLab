package lab.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import lab.base.util.HttpStatus;

/**
 * HTTP结果封装
 */
public class HttpResult {

	private int code = 0;
	private String msg;

	private Object payload;

	private Object inform;

	//线程安全的
    //类初始化时，立即加载这个对象
    private static HttpResult instance = new HttpResult();

    private HttpResult() {

    }

    //方法没有加同步块，所以它效率高
    public static HttpResult getInstance() {
        return instance;
    }

	public static HttpResult error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public HttpResult error1() {
		return error(this.getCode(), this.getMsg());
	}

	public static HttpResult error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static HttpResult error(int code, String msg) {
		HttpResult r = new HttpResult();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}

	public static HttpResult ok(Object data) {
		HttpResult r = new HttpResult();
		r.setPayload(data);
		return r;
	}

	public static HttpResult ok() {
		return new HttpResult();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public Object getInform() {
		return inform;
	}

	public void setInform(Object inform) {
		this.inform = inform;
	}


	public String toString(){
		//return JSONObject.toJSONString(getInstance());
		return JSON.toJSONString(getInstance(), SerializerFeature.WriteMapNullValue);
	}
}
