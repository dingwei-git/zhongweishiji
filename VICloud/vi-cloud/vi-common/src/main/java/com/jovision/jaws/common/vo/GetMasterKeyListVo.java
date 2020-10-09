package com.jovision.jaws.common.vo;

import com.jovision.jaws.common.po.CodeMsgPo;
import com.jovision.jaws.common.po.MkListPo;
import lombok.Data;

/**
 * 获取服务相对应的主密钥列表
 * @author : fengyefeiluo
 * @ClassName DecprytConfigurationFieldsVo
 * @Version 1.0
 * @Date : 2020/1/20 15:10
 * @UpdateDate : 2020/1/20 15:10
 * @Version V1.0.0
 **/
@Data
public class GetMasterKeyListVo {
    /**
     * 方法名
     */
    private String method;
    /**
     * 消息id 非必须
     */
    private String id;
    /**
     * 传参
     */
    private MkListPo result;

    private CodeMsgPo error;
}
