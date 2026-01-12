package cn.lazyking.power.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lazyking.power.mapper.ProdTagReferenceMapper;
import cn.lazyking.power.domain.ProdTagReference;
import cn.lazyking.power.service.impl.ProdTagReferenceService;
@Service
public class ProdTagReferenceServiceImpl extends ServiceImpl<ProdTagReferenceMapper, ProdTagReference> implements ProdTagReferenceService{

}
