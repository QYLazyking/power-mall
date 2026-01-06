package cn.lazyking.model;

import cn.lazyking.constants.BusinessStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一响应结果对象")
public class Result<T> {

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static<T> Result<T> fail(BusinessStatus businessStatus) {
        return new Result<>(businessStatus.getCode(), businessStatus.getMsg(), null);
    }

}
