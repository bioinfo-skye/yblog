package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiData;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.user;
import com.boot.service.userService;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 游政杰
 */
@Controller("pearUserManagerController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class userManagerController {

    @Autowired
    private userService userService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    private Logger logger = Logger.getLogger(userManagerController.class);


    @ResponseBody
    @RequestMapping(path = "/userManagerData")
    public String userManagerData(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "limit", defaultValue = "6") int limit,
                                  @RequestParam(value = "username", defaultValue = "") String username,
                                  @RequestParam(value = "email", defaultValue = "") String email,
                                  HttpSession session) {
        if (StringUtils.isBlank(username) && StringUtils.isBlank(email)) { //这种情况查询全部
            layuiData<user> json = new layuiData<>();

            PageHelper.startPage(page, limit);
            List<user> users = userService.selectAllUser();
            String name = springSecurityUtil.currentUser(session);

            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            logger.debug(time + "   用户名：" + name + "进入后台用户管理页面");

            int total = userService.userCount();

            json.setCode(0);
            json.setCount(total); //总数
            json.setMsg("");
            json.setData(users); //分页数据

            return JSON.toJSONString(json);
        } else { //查询条件

            layuiData<user> userlayuiData = new layuiData<user>();

            List<user> users = userService.selectUserByUsernameAndEmail(username, email);

            int count = userService.selectUserCountByUsernameAndEmail(username, email);

            userlayuiData.setCode(0);
            userlayuiData.setMsg("");
            userlayuiData.setData(users);
            userlayuiData.setCount(count);
            return JSON.toJSONString(userlayuiData);

        }


    }


    @GetMapping(path = "/enable/Vaild")
    @ResponseBody
    public String updateVaildEnable(String name,
                                    HttpSession session) {


        layuiJSON json = new layuiJSON();

        try {

            userService.updateValidTo_1(name);
            json.setMsg("66");
            json.setSuccess(true);
        } catch (Exception e) {

            e.printStackTrace();
            json.setMsg("66");
            json.setSuccess(false);
        }

        return JSON.toJSONString(json);
    }

    @GetMapping(path = "/disable/Vaild")
    @ResponseBody
    public String updateVaildDisable(String name,
                                     HttpSession session) {
        layuiJSON json = new layuiJSON();

        try {

            userService.updateValidTo_0(name);
            json.setMsg("77");
            json.setSuccess(true);
        } catch (Exception e) {

            e.printStackTrace();
            json.setMsg("77");
            json.setSuccess(false);
        }

        return JSON.toJSONString(json);
    }

}
