package com.zlq.mall.common.utils;

import com.zlq.mall.common.constant.RespCodeConstant;

import java.util.Map;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.common.utils
 * @ClassName: DaoResult
 * @description:
 * @author: LiQun
 * @CreateDate:2022/12/10 22:51
 */
public class OperationResult extends R {
    public OperationResult() {
        super();
    }

    public static OperationResult error() {
        return error(RespCodeConstant.INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static OperationResult error(String msg) {
        return error(RespCodeConstant.INTERNAL_SERVER_ERROR, msg);
    }

    public static OperationResult error(int code, String msg) {
        OperationResult result = new OperationResult();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public static OperationResult ok(String msg) {
        OperationResult result = new OperationResult();
        result.put("msg", msg);
        return result;
    }

    public static OperationResult ok(Map<String, Object> map) {
        OperationResult result = new OperationResult();
        result.putAll(map);
        return result;
    }

    public static OperationResult ok() {
        return new OperationResult();
    }


    @Override
    public OperationResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public Integer getCode() {
        return super.getCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }
}
