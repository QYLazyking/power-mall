package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.ProdProp;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdPropService extends IService<ProdProp>{


    Boolean addProdProp(ProdProp prodProp);

    boolean modifyProdProp(ProdProp prodProp);

    boolean removeProdProp(Long propId);

    Page<ProdProp> getProdSpecPage(Long current, Long size, String propName);
}
