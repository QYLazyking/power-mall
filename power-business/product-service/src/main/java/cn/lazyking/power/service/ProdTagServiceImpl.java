package cn.lazyking.power.service;

import cn.lazyking.power.domain.ProdTag;
import cn.lazyking.power.mapper.ProdTagMapper;
import cn.lazyking.power.service.impl.ProdTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService{

    @Override
    public boolean saveProdTag(ProdTag prodTag) {
        Date date = new Date();
        prodTag.setCreateTime(date);
        prodTag.setUpdateTime(date);
        return this.save(prodTag);
    }

    @Override
    public ProdTag getProdTagDetailById(Long prodTagId) {
        return this.getById(prodTagId);
    }
}
