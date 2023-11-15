package lab.service;

import lab.base.util.HttpStatus;
import lab.entity.HttpResult;
import lab.po.userPO;
import lab.po.userPO2;
import lab.po.userUploadPO;
import lab.vo.userVO;
import lab.dao.uUserDao;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class userService {

	@Autowired
	private uUserDao uUserDao;

	public HttpResult selectUser(userPO userPO){
		HttpResult result = HttpResult.getInstance();
		try {
			result.setPayload(uUserDao.findByUsername(userPO));
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg(e.toString());
		}
		return result;
	}

	public HttpResult selectUser2(userPO userPO){
		HttpResult result = HttpResult.getInstance();
		try {
			result.setPayload(uUserDao.findByUsername2(userPO));
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg(e.toString());
		}
		return result;
	}

	public List<userVO> exportExcel(userPO userPO){
		return uUserDao.exportExcel(userPO);
	}

	public HttpResult insertUser(userPO2 userPO2){
		HttpResult result = HttpResult.getInstance();
		try {
			int repeatRow = uUserDao.findUsername(userPO2.getUsername(), "");
			if(repeatRow > 0){
				if(result.getCode() == 0){
					result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				}
				result.setMsg("用户名" + userPO2.getUsername() + "已存在，请检查");
				return result;
			}

			userPO2.setPassword(DigestUtils.md5Hex(userPO2.getPassword()).toUpperCase());
			uUserDao.insertUser(userPO2);
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
			result.setMsg("用户" + userPO2.getUsername() + "新建成功");
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("用户" + userPO2.getUsername() + "新建失败," + e.toString());
		}
		return result;
	}

	public HttpResult insertUsers(List<userUploadPO> userUploadPO){
		HttpResult result = HttpResult.getInstance();
		try {
			for(int i = 0;i < userUploadPO.size(); i++){
				int repeatRow = uUserDao.findUsername(userUploadPO.get(i).getUsername(), "");
				if(repeatRow > 0){
					if(result.getCode() == 0){
						result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					}
					result.setMsg("用户名已存在，请检查");
					return result;
				}
				userUploadPO.get(i).setPassword(DigestUtils.md5Hex(userUploadPO.get(i).getPassword()).toUpperCase());
			}

			uUserDao.insertUsers(userUploadPO);
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
			result.setMsg("批量新建用户操作成功");
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("批量新建用户操作失败," + e.toString());
		}
		return result;
	}

	public HttpResult updUser(userPO2 userPO2){
		HttpResult result = HttpResult.getInstance();
		try {
			uUserDao.updUser(userPO2);
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
			result.setMsg("用户" + userPO2.getUsername() + "更新成功");
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("用户" + userPO2.getUsername() + "更新失败请检查数据," + e.toString());
		}
		return result;
	}

	public void updPwd(userPO2 userPO2) throws Exception{
		uUserDao.updPwd(userPO2);
	}

	public HttpResult delUser(userPO userPO){
		HttpResult result = HttpResult.getInstance();
		try {
			uUserDao.delUser(userPO);
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
			result.setMsg("用户" + userPO.getUsername() + "删除成功");
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("用户" + userPO.getUsername() + "删除失败");
		}
		return result;
	}

	//下面这一行的意义是加入数据库事务
	//表示当此方法内包括里面任意子方法如果报错，将会回滚一整个事务
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public HttpResult file(MultipartFile file) {
		HttpResult result = HttpResult.getInstance();
		int repeatRow = 0;
		JSONObject json = new JSONObject();
		byte[] bytes = new byte[1024];
		InputStream ins = null;
		try {
			//获取文件
			ins = file.getInputStream();
			XSSFWorkbook book = new XSSFWorkbook(ins);
			XSSFSheet sheet = book.getSheetAt(0);
			int rowNum = sheet.getLastRowNum() + 1;
			List<userUploadPO> list = new ArrayList<userUploadPO>();
			//获取每一行的数据
			for (int j = 1; j < rowNum; j++) {
				XSSFRow row = sheet.getRow(j);
				if (row != null) {
					//获取每一行中每一列的数据
					userUploadPO po = new userUploadPO();
					repeatRow = uUserDao.findUsername(row.getCell(0).toString(), "");
					//System.out.println(repeatRow);
					if(repeatRow > 0){
						if(result.getCode() == 0){
							result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
						}
						result.setMsg("第" + repeatRow + "行用户名已存在，请检查");
						return result;
					}
					po.setUsername(row.getCell(0).toString());
					po.setName(row.getCell(1).toString());
					po.setPassword(DigestUtils.md5Hex(row.getCell(2).toString()).toUpperCase());
					list.add(po);
				}
			}
			//插入数据库
			insertUsers(list);
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("后台无法访问此文件");
		} catch (IOException ie) {
			ie.printStackTrace();
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg("处理文件出错了");
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
					e.printStackTrace();
					if(result.getCode() == 0){
						result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					}
					result.setMsg("未知异常");
				}
			}
		}
		if(result.getCode() == 0){
			result.setCode(HttpStatus.SC_OK);
		}
		result.setMsg("批量新建用户操作成功");
		return result;
	}

	/**查询用户个数*/
	public HttpResult findUserNum(String username, String name){
		HttpResult result = HttpResult.getInstance();
		try {
			result.setPayload(uUserDao.findUsername(username, name));
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_OK);
			}
		} catch (Exception e){
			if(result.getCode() == 0){
				result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			result.setMsg(e.toString());
		}
		return result;
	}
}
