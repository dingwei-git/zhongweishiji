package com.jovision.jaws.common.dto;

import com.jovision.jaws.common.po.NamespacePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 获取服务相对应的主密钥列表
 * @author : fengyefeiluo
 * @ClassName GetMasterKeyListDto
 * @Version 1.0
 * @Date : 2020/1/20 15:10
 * @UpdateDate : 2020/1/20 15:10
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMasterKeyListDto {
    /**
     * 方法名
     */
    @NotNull
    private String method;
    /**
     * 消息id 非必须
     */
    private String id;
    /**
     * 传参
     */
    @NotNull
    private NamespacePo params;
}
