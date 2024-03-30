package com.wzj.mvvm_test.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 基础返回类
 * @SerializedName() 将 Java 对象的成员字段与 JSON 数据中的键进行映射。
 * @Expose  该字段将被 Gson 序列化和反序列化。
 */
public class BaseResponse {
    @SerializedName("res_code")
    @Expose
    public Integer responseCode;
    @SerializedName("res_error")
    @Expose
    public String responseError;
}
