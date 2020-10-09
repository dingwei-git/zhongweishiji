package com.jovision.jaws.common.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author : fengyefeiluo
 * @ClassName NamespacePo
 * @Version 1.0
 * @Date : 2020/1/20 15:12
 * @UpdateDate : 2020/1/20 15:12
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NamespacePo {
    /**
     * 服务相对应的namespace名字
     */
    @NotNull
    private String namespace;
}
