package com.zz.amqp1.controller;

import com.zz.amqp1.bean.XhCodeIModel;
import com.zz.amqp1.bean.XhCodeOModel;
import com.zz.amqp1.config.Response;
import com.zz.amqp1.service.XhCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 * Description:玺号专用(基础服务层51服务器, 服务名: basic-data-platform )
 * User: zhouzhou
 * Date: 2018-09-19
 * Time: 13:07
 */

@RestController
@RequestMapping("/xhCode")
public class XhCodeController {

    @Autowired
    private XhCodeService xhCodeService;


    @RequestMapping(value = "/{cityCode}", method = RequestMethod.GET)
    public Response<XhCodeOModel> getMaxSignatureNo(@PathVariable("cityCode") String cityCode) {
        Response<XhCodeOModel> response = new Response<>();
        response.setCode(Response.HttpResponseStatus.OK);
        XhCodeOModel xhCodeOModel = xhCodeService.findMaxSignatureNo(cityCode);
        if (xhCodeOModel == null) {
            response.setMsg("根据城市编码{%s}暂无对应玺号");
            return response;
        }
        response.setMsg(String.format("当前城市编码{%s}对应的玺号是{%s}", cityCode, xhCodeOModel.getXhCode()));
        response.setData(xhCodeOModel);
        return response;
    }

    @RequestMapping(value = "/{xhCode}", method = RequestMethod.POST)
    public Response<XhCodeOModel> createSignatureNo(@PathVariable("xhCode") String xhCode, @RequestBody XhCodeIModel xhCodeIModel) {
        Response<XhCodeOModel> response = new Response<>();
        XhCodeOModel xhCodeOModel = xhCodeService.createSignatureNo(xhCode, xhCodeIModel);
        response.setCode(Response.HttpResponseStatus.OK);
        response.setData(xhCodeOModel);
        response.setMsg(String.format("恭喜您创建成功,所对应的玺号为{%s}", xhCodeOModel.getXhCode()));
        return response;
    }


    @RequestMapping(value = "/generateMaxXhCode/{cityCode}", method = RequestMethod.POST)
    public Response<XhCodeOModel> generateMaxXhCodeByCityCode(@PathVariable("cityCode") String cityCode) {
        Response<XhCodeOModel> response = new Response<>();
        XhCodeOModel xhCodeOModel = xhCodeService.generateMaxXhCode(cityCode);
        response.setCode(Response.HttpResponseStatus.OK);
        response.setData(xhCodeOModel);
        response.setMsg(String.format("恭喜您创建成功,所对应的玺号为{%s}", xhCodeOModel.getXhCode()));
        return response;
    }

}
