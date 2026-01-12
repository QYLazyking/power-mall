package cn.lazyking.power.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lazyking.power.mapper.SkuMapper;
import cn.lazyking.power.domain.Sku;
import cn.lazyking.power.service.impl.SkuService;
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService{

}
