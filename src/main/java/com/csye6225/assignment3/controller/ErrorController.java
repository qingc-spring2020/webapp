package com.csye6225.assignment3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletResponse response){
        //获取statusCode:401,404,500
        response.setStatus(200);
        return "welcome";

    }
    @Override
    public String getErrorPath() {
        return "/error";
    }

}
