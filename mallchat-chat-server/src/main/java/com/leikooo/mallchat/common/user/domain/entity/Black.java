package com.leikooo.mallchat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2023-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("black")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Black implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 拉黑目标类型 1.ip 2uid
     *
     * @see com.leikooo.mallchat.common.user.domain.enums.BlackTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 拉黑目标
     */
    @TableField("target")
    private String target;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


}
