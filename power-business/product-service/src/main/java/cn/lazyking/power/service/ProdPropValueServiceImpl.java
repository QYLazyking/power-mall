package cn.lazyking.power.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lazyking.power.mapper.ProdPropValueMapper;
import cn.lazyking.power.domain.ProdPropValue;
import cn.lazyking.power.service.impl.ProdPropValueService;
@Service
public class ProdPropValueServiceImpl extends ServiceImpl<ProdPropValueMapper, ProdPropValue> implements ProdPropValueService{

}
