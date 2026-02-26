package com.bjdj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家集团基本信息表
 * </p>
 *
 * @author bpzhang
 * @since 2025-04-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tbl_shop_group_info")
public class ShopGroupInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自增,集团ID
     */
    @TableField("group_id")
    private Integer groupId;

    /**
     * 集团名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     *  集团简称
     */
    @TableField("group_short_name")
    private String groupShortName;

    /**
     * 所在城市id
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 集团联系人
     */
    @TableField("link_man")
    private String linkMan;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 商家客服电话
     */
    @TableField("service_tel")
    private String serviceTel;

    /**
     * 商家地址
     */
    @TableField("address")
    private String address;

    /**
     * 运作模式 0：直营 1：代理商
     */
    @TableField("operation_model")
    private Byte operationModel;

    /**
     * 经营模式 1：单店 2：多店
     */
    @TableField("business_model")
    private Byte businessModel;

    /**
     * 代理商名称
     */
    @TableField("agent")
    private String agent;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
