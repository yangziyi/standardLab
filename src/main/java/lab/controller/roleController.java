package lab.controller;

import lab.po.rolePO;
import lab.po.roleUserPO;
import lab.po.roleUserSelUpdPO;
import lab.service.roleService;
import lab.vo.roleVO;
import lab.vo.userVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class roleController {
	@Autowired
	private roleService roleService;

	@RequestMapping(value = "/findRoleCount" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Integer findRoleCount(@RequestBody rolePO rolePO) throws Exception{
		return roleService.findRoleCount(rolePO);
	}

	@RequestMapping(value = "/findRole" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<roleVO> findRole(@RequestBody rolePO rolePO) throws Exception{
		return roleService.findRole(rolePO);
	}

	@RequestMapping(value = "/insRole" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void insRole(@RequestBody rolePO rolePO) throws Exception{
		roleService.insRole(rolePO);
	}

	@RequestMapping(value = "/updRole" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void updRole(@RequestBody rolePO rolePO) throws Exception{
		roleService.updRole(rolePO);
	}

	@RequestMapping(value = "/delRole" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void delRole(@RequestBody rolePO rolePO) throws Exception{
		roleService.delRole(rolePO);
	}

	@RequestMapping(value = "/findRoleUsers" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<userVO> findRoleUsers(@RequestBody roleUserSelUpdPO roleUserSelUpdPO) throws Exception{
		return roleService.findRoleUsers(roleUserSelUpdPO);
	}

	@RequestMapping(value = "/findRoleUserCount" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Integer findRoleUserCount(@RequestBody roleUserSelUpdPO roleUserSelUpdPO) throws Exception{
		return roleService.findRoleUserCount(roleUserSelUpdPO);
	}

	@RequestMapping(value = "/findNoRoleUsers" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<userVO> findNoRoleUsers(@RequestBody roleUserSelUpdPO roleUserSelUpdPO) throws Exception{
		return roleService.findNoRoleUsers(roleUserSelUpdPO);
	}

	@RequestMapping(value = "/insRoleUsers" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void insRoleUsers(@RequestBody roleUserPO roleUserPO) throws Exception{
		roleService.insRoleUsers(roleUserPO);
	}

	@RequestMapping(value = "/delRoleUsers" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void delRoleUsers(@RequestBody roleUserSelUpdPO roleUserSelUpdPO) throws Exception{
		roleService.delRoleUsers(roleUserSelUpdPO);
	}

}
