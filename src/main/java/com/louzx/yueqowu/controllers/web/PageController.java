package com.louzx.yueqowu.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 1Zx.
 * @date 2020/10/14 14:16
 */

@Controller
@RequestMapping("page")
public class PageController {

    @GetMapping("index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @GetMapping("brandInfo")
    public ModelAndView brandInfo(Integer brandId) {
        ModelAndView modelAndView = new ModelAndView("brandInfo");
        modelAndView.addObject("brandId", brandId);
        return modelAndView;
    }

    @GetMapping("sInfo")
    public ModelAndView sInfo(String goodsId){
        ModelAndView modelAndView = new ModelAndView("sInfo");
        modelAndView.addObject("goodsId", goodsId);
        return modelAndView;
    }
}
