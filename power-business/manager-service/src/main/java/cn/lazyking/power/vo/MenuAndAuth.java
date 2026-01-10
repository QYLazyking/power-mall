package cn.lazyking.power.vo;


import cn.lazyking.power.domain.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("菜单和操作权限对象")
@Builder
public class MenuAndAuth {
    @ApiModelProperty("菜单权限集合")
    private Set<String> menus;

    @ApiModelProperty("操作权限集合")
    private List<SysMenu> authorities;
}
