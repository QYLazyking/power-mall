package cn.lazyking.power.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("登录结果响应对象")
public class LoginResult {

    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("令牌过期时间")
    private Long expireIn;
}
