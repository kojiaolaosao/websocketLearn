package com.example.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @program: learnwebsokect
 * @description: CheckCenterController
 * @author: Mr.qi
 * @create: 2021-08-10 22:16
 **/

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid){
        ModelAndView mav = new ModelAndView("socket");
        mav.addObject("cid", cid);
        return mav;
    }

    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message){
        try {
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
