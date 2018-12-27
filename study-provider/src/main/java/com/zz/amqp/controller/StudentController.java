package com.zz.amqp.controller;

import com.zz.amqp.annotation.ZAutowired;
import com.zz.amqp.annotation.ZController;
import com.zz.amqp.annotation.ZRequestMapping;
import com.zz.amqp.annotation.ZRequestParam;
import com.zz.amqp.service.StudentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-24
 * Time: 16:53
 */
@ZController
@ZRequestMapping("/student")
public class StudentController {

    @ZAutowired
    private StudentService studentService;

    @ZRequestMapping("/query")
    public void  query(HttpServletRequest request, HttpServletResponse response, @ZRequestParam String name){
        String result = studentService.query(name);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
