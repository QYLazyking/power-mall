package cn.lazyking.power.service;

import cn.lazyking.power.domain.ProdComm;
import cn.lazyking.power.mapper.ProdCommMapper;
import cn.lazyking.power.service.impl.ProdCommService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService{

    @Override
    public boolean examineOrReplyProdComm(ProdComm prodComm) {
        // 获取回复内容
        String replyContent = prodComm.getReplyContent();
        // 设置回复属性
        if(StringUtils.hasText(replyContent)) {
            prodComm.setReplyTime(new Date());
            prodComm.setReplySts(1);
        }
        return this.updateById(prodComm);
    }
}
