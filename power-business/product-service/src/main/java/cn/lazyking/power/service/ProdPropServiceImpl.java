package cn.lazyking.power.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.lazyking.power.domain.ProdProp;
import cn.lazyking.power.domain.ProdPropValue;
import cn.lazyking.power.mapper.ProdPropMapper;
import cn.lazyking.power.service.impl.ProdPropService;
import cn.lazyking.power.service.impl.ProdPropValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService{

    private final ProdPropMapper prodPropMapper;
    private final ProdPropValueService prodPropValueService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addProdProp(ProdProp prodProp) {
        // 设置商品属性
        prodProp.setShopId(1L);
        prodProp.setRule(1);

        boolean result = this.save(prodProp);
        if (result) {
            // 获取 id
            Long id = prodProp.getPropId();
            // 获取商品属性值集合
            List<ProdPropValue> prodPropValues = prodProp.getProdPropValues();
            // 补充属性 id
            prodPropValues.forEach(prodPropValue -> prodPropValue.setPropId(id));
            // 批量添加商品属性值
            prodPropValueService.saveBatch(prodPropValues);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyProdProp(ProdProp prodProp) {
        // 删除旧属性值
        Long propId = prodProp.getPropId();
        prodPropValueService.remove(
                new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, propId)
        );

        // 给新的属性值补充属性 id
        List<ProdPropValue> prodPropValues = prodProp.getProdPropValues();
        prodPropValues.forEach(value -> value.setPropId(propId));
        // 批量保存
        prodPropValueService.saveBatch(prodPropValues);
        // 更新 ProdProp
        return this.updateById(prodProp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProdProp(Long propId) {
        // 删除属性值
        prodPropValueService.remove(
                new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, propId)
        );

        // 根据 id 删除属性
        return this.removeById(propId);
    }

    @Override
    public Page<ProdProp> getProdSpecPage(Long current, Long size, String propName) {
        Page<ProdProp> page = new Page<>(current, size);
        page = this.page(
                page,
                new LambdaQueryWrapper<ProdProp>()
                        .like(StringUtils.hasText(propName), ProdProp::getPropName, propName)
        );

        List<ProdProp> prodPropList = page.getRecords();
        if(CollectionUtil.isEmpty(prodPropList)) {
            return page;
        }

        List<Long> propIds = prodPropList.stream().map(ProdProp::getPropId).toList();
        List<ProdPropValue> prodPropValues = prodPropValueService.list(
                new LambdaQueryWrapper<ProdPropValue>()
                        .in(ProdPropValue::getPropId, propIds)
        );

        prodPropList.forEach(prodProp -> {
            Long id = prodProp.getPropId();
            List<ProdPropValue> valueList = prodPropValues.stream()
                    .filter(value -> value.getPropId().equals(id))
                    .toList();
            prodProp.setProdPropValues(valueList);
        });

        return page;
    }
}
