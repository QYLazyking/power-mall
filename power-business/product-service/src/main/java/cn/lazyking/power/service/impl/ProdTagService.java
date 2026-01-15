package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.ProdTag;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdTagService extends IService<ProdTag>{


    boolean saveProdTag(ProdTag prodTag);

    ProdTag getProdTagDetailById(Long prodTagId);
}
