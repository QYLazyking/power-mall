package cn.lazyking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {
    // 商城后台管理系统用户的相关属性
    private Long userId;
    private String username;
    private String password;
    private Byte status;
    private Long shopId;
    private String loginType;
    private Set<String> perms = new HashSet<>();

    // 商城购物会员用户的相关属性


    public Set<String> getPerms() {
        HashSet<String> result = new HashSet<>();

        perms.stream()
            .flatMap(perm -> perm.contains(",") ? Arrays.stream(perm.split(",")) : Stream.of(perm))
            .forEach(result::add);

        return result;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginType + this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账户未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭据未过期
    }

    @Override
    public boolean isEnabled() {
        // 根据 status 字段判断账户是否启用，假设 1 表示启用，0 表示禁用
        return this.status != null && this.status == 1;
    }

}
