package cn.lazyking.power.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.lazyking.power.domain.Prod;
import cn.lazyking.power.domain.ProdTagReference;
import cn.lazyking.power.domain.Sku;
import cn.lazyking.power.mapper.ProdMapper;
import cn.lazyking.power.service.impl.ProdService;
import cn.lazyking.power.service.impl.ProdTagReferenceService;
import cn.lazyking.power.service.impl.SkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{

    private final ProdTagReferenceService prodTagReferenceService;
    private final SkuService skuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProd(Prod prod) {
        // 新增商品
        prod.setShopId(1L);
        prod.setSoldNum(0);
        // 设置配送方式
        Prod.DeliveryModeVo deliveryMode = prod.getDeliveryModeVo();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            prod.setDeliveryMode(objectMapper.writeValueAsString(deliveryMode));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Date now = new Date();
        prod.setCreateTime(now);
        prod.setUpdateTime(now);
        prod.setPutawayTime(now);
        prod.setVersion(0);
        boolean result = this.save(prod);
        if (result) {
            // 获取商品 id
            Long prodId = prod.getProdId();
            // 获取商品分组标签 id 集合
            List<Long> tagList = prod.getTagList();
            if(CollectionUtil.isNotEmpty(tagList)) {
                // 创建商品与分组标签关系集合
                List<ProdTagReference> referenceList = tagList.stream().map(tagId -> {
                    // 创建 ProdTagReference 对象
                    ProdTagReference reference = new ProdTagReference();
                    reference.setProdId(prodId);
                    reference.setTagId(tagId);
                    reference.setShopId(1L);
                    reference.setCreateTime(now);
                    reference.setStatus(1);
                    return reference;
                }).toList();
                // 批量保存
                prodTagReferenceService.saveBatch(referenceList);
            }

            // 获取商品 sku 对象集合
            List<Sku> skuList = prod.getSkuList();
            if(CollectionUtil.isNotEmpty(skuList)) {
              skuList.forEach(sku -> {
                    sku.setProdId(prodId);
                    sku.setActualStocks(sku.getStocks());
                    sku.setCreateTime(now);
                    sku.setUpdateTime(now);
                    sku.setVersion(0);
              });
              skuService.saveBatch(skuList);
            }
        }

        return result;
    }

    @Override
    public Prod getProdDetailById(Long prodId) {
        // 根据 id 查询商品
        Prod prod = this.getById(prodId);
        if(prod == null) {
            return null;
        }

        // 根据商品 id 查询商品与分组标签关系集合
        List<ProdTagReference> referenceList = prodTagReferenceService.list(
                new LambdaQueryWrapper<ProdTagReference>()
                        .eq(ProdTagReference::getProdId, prodId)
        );
        if(CollectionUtil.isNotEmpty(referenceList)) {
            List<Long> referenceIdList = referenceList
                    .stream()
                    .map(ProdTagReference::getTagId)
                    .toList();
            prod.setTagList(referenceIdList);
        }

        // 根据商品 id 查询商品 sku 集合
        List<Sku> skuList = skuService.list(
                new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getProdId, prodId)
        );
        if(CollectionUtil.isNotEmpty(skuList)) {
            skuList.forEach(sku -> sku.setActualStocks(sku.getStocks()));
            prod.setSkuList(skuList);
        }

        return prod;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyProd(Prod prod) {
        // 获取商品标识
        Long prodId = prod.getProdId();
        // 删除原有的分组标签关系
        prodTagReferenceService.remove(
                new LambdaQueryWrapper<ProdTagReference>()
                        .eq(ProdTagReference::getProdId, prodId)
        );

        Date now = new Date();

        // 获取商品分组标签 id 集合
        List<Long> tagList = prod.getTagList();
        if(CollectionUtil.isNotEmpty(tagList)) {
            // 闯将商品与分组标签关系集合对象
            List<ProdTagReference> referenceList = tagList.stream().map(tagId -> {
                // 创建 ProdTagReference 对象
                ProdTagReference reference = new ProdTagReference();
                reference.setProdId(prodId);
                reference.setTagId(tagId);
                reference.setShopId(1L);
                reference.setCreateTime(now);
                reference.setStatus(1);
                return reference;
            }).toList();
            prodTagReferenceService.saveBatch(referenceList);
        }

        // 删除原有商品 sku 对象
        skuService.remove(
                new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getProdId, prodId)
        );

        // 获取商品 sku 集合
        List<Sku> skuList = prod.getSkuList();
        if(CollectionUtil.isNotEmpty(skuList)) {
            skuList.forEach(sku -> {
                sku.setProdId(prodId);
                sku.setActualStocks(sku.getStocks());
                sku.setCreateTime(now);
                sku.setUpdateTime(now);
                sku.setVersion(0);
            });
            skuService.saveBatch(skuList);
        }

        // 更新商品信息
        prod.setUpdateTime(now);
        return this.updateById(prod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProdsByIds(List<Long> prodIds) {
        prodTagReferenceService.remove(
                new LambdaQueryWrapper<ProdTagReference>()
                        .in(ProdTagReference::getProdId, prodIds)
        );
        skuService.remove(
                new LambdaQueryWrapper<Sku>()
                        .in(Sku::getProdId, prodIds)
        );
        return this.removeBatchByIds(prodIds);
    }

}
