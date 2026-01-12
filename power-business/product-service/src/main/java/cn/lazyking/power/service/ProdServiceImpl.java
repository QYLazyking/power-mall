package cn.lazyking.power.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lazyking.power.domain.Prod;
import cn.lazyking.power.mapper.ProdMapper;
import cn.lazyking.power.service.impl.ProdService;
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{

}
