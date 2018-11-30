package com.zz.amqp1.config;

import java.io.Serializable;


public class Response<T> implements Serializable {

    private HttpResponseStatus code;
    private String msg;
    private T data;
    private Object links;

    public Response() {
    }

    public Response(HttpResponseStatus code) {
        this.code = code;
    }

    public Response(HttpResponseStatus code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(HttpResponseStatus code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(HttpResponseStatus code, String msg, T data, Object links) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.links = links;
    }

    public HttpResponseStatus getCode() {
        return code;
    }

    public void setCode(HttpResponseStatus code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getLinks() {
        return links;
    }

    public void setLinks(Object links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", links=" + links +
                '}';
    }



    public enum HttpResponseStatus {
        OK("0", "ok"),

        NO_SERVICE("8", "no service"),

        ERR("9", "error");

        private final String value;
        private final String description;

        HttpResponseStatus(String value, String description) {
            this.value = value;
            this.description = description;
        }

        public String value() {
            return this.value;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
