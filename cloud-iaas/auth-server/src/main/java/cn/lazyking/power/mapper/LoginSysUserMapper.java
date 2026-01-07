package cn.lazyking.power.mapper;

import cn.lazyking.power.domain.LoginSysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface LoginSysUserMapper extends BaseMapper<LoginSysUser> {
    Set<String> selectPermsByUserId(Long userId);
}