package com.zlq.mall.product.constant;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.product.constant
 * @ClassName: UpdateCategoryBrandEnum
 * @description:
 * @author: LiQun
 * @CreateDate:2022/12/10 23:39
 */
public enum UpdateCategoryBrandEnum {
    UPDATE_CATEGORY(0,"修改分类数据"),
    UPDATE_BRAND(1,"修改品牌数据");
    private int code;
    private String msg;

    UpdateCategoryBrandEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
