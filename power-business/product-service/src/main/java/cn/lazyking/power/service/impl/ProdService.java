package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProdService extends IService<Prod>{

    boolean addProd(Prod prod);

    Prod getProdDetailById(Long prodId);

    boolean modifyProd(Prod prod);

    boolean removeProdsByIds(List<Long> prodIds);
}
