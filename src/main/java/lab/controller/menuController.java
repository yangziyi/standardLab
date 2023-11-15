package lab.controller;

import lab.po.menuPO;
import lab.po.rolePO;
import lab.service.menuService;
import lab.vo.menuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class menuController {
	@Autowired
	private menuService menuService;

	@RequestMapping(value = "/findMenu" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<menuVO> findMenu(@RequestBody menuPO menuPO) throws Exception{
		return menuService.findMenu(menuPO);
	}

	@RequestMapping(value = "/insMenu" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void insMenu(@RequestBody menuPO menuPO) throws Exception{
		menuService.insMenu(menuPO);
	}

	@RequestMapping(value = "/updMenu" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void updMenu(@RequestBody menuPO menuPO) throws Exception{
		menuService.updMenu(menuPO);
	}

	@RequestMapping(value = "/delMenu" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void delMenu(@RequestBody menuPO menuPO) throws Exception{
		menuService.delMenu(menuPO);
	}

}
