package cn.lazyking.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description="points_prod")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "points_prod")
public class PointsProd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品id
     */
    @TableId(value = "points_prod_id", type = IdType.INPUT)
    @ApiModelProperty(value="积分商品id")
    private Long pointsProdId;

    /**
     * 所需积分id
     */
    @TableField(value = "points_id")
    @ApiModelProperty(value="所需积分id")
    private Long pointsId;

    /**
     * 所需积分量
     */
    @TableField(value = "points_number")
    @ApiModelProperty(value="所需积分量")
    private Double pointsNumber;

    /**
     * 所需金额
     */
    @TableField(value = "amount")
    @ApiModelProperty(value="所需金额")
    private BigDecimal amount;

    /**
     * 关联商品id
     */
    @TableField(value = "prod_id")
    @ApiModelProperty(value="关联商品id")
    private Long prodId;

    /**
     * 库存
     */
    @TableField(value = "stocks")
    @ApiModelProperty(value="库存")
    private Integer stocks;

    /**
     * 状态(0下架 1上架)
     */
    @TableField(value = "`state`")
    @ApiModelProperty(value="状态(0下架 1上架)")
    private Integer state;

    /**
     * 上架时间
     */
    @TableField(value = "upper_shelf_time")
    @ApiModelProperty(value="上架时间")
    private Date upperShelfTime;

    /**
     * 下架时间
     */
    @TableField(value = "lower_shelf")
    @ApiModelProperty(value="下架时间")
    private Date lowerShelf;
}