package lab.controller;

import lab.base.util.aspect.serviceAround;
import lab.entity.HttpResult;
import lab.po.userPO;
import lab.po.userPO2;
import lab.service.userService;
import lab.service.userExcelWrite;
import lab.vo.userVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {
	@Autowired
	private userService userService;
	@Autowired
	private userExcelWrite userExcelWrite;

	@RequestMapping(value = "/selectUser" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public HttpResult c(@RequestBody userPO userPO) {
		System.out.println(userPO.getName());
		return userService.selectUser(userPO);
	}

	@RequestMapping(value = "/selectUser2" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public HttpResult c2(@RequestBody userPO userPO) {
		return userService.selectUser2(userPO);
	}

	@serviceAround
	@RequestMapping(value = "/insertUser" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public HttpResult insUser(@RequestBody userPO2 userPO2) {
		return userService.insertUser(userPO2);
	}

	@serviceAround
	@RequestMapping(value = "/updUser" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public HttpResult updUser(@RequestBody userPO2 userPO2) {
		return userService.updUser(userPO2);
	}

	@serviceAround
	@RequestMapping(value = "/updPwd" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void updPwd(@RequestBody userPO2 userPO2) throws Exception{
		userService.updPwd(userPO2);
	}

	@serviceAround
	@RequestMapping(value = "/delUser" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public HttpResult delUser(@RequestBody userPO userPO) {
		return userService.delUser(userPO);
	}

	@serviceAround
	@RequestMapping(value = "/file" , method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
	public HttpResult file(@RequestParam("myfile") MultipartFile file) {
		return userService.file(file);
	}

	@RequestMapping(value = "/findUserNum" , method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
	public HttpResult findUserNum(@RequestBody Map<String, String> map){
		return userService.findUserNum(map.get("username"), map.get("name"));
	}

	@serviceAround
	@RequestMapping(value = "/exportUserExcel" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void exportUserExcel(@RequestBody userPO userPO, HttpServletResponse response)
			throws IOException {
		List<userVO> userList = userService.exportExcel(userPO);
		// 以流的形式下载文件。
		InputStream fis = userExcelWrite.userExcelWrite(userList);
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 设置response的Header
		response.addHeader("Content-Disposition",
				"attachment;filename=人员维护表.xlsx");
		response.setContentType("application/octet-stream");
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	}

}
