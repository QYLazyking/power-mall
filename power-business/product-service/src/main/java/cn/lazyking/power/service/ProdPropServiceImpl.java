package cn.lazyking.power.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lazyking.power.domain.ProdProp;
import cn.lazyking.power.mapper.ProdPropMapper;
import cn.lazyking.power.service.impl.ProdPropService;
@Service
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService{

}
