package com.jovision.jaws.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : fengyefeiluo
 * @ClassName MasterKeyVo
 * @Version 1.0
 * @Date : 2020/1/20 16:42
 * @UpdateDate : 2020/1/20 16:42
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterKeyVo {
    /**
     * 主密钥ID
     */
    private String mk_id;
    /**
     * 主密钥明文
     */
    private String mk_data;
    /**
     * 是否是最新的主密钥，true代表是
     */
    private Boolean latest;
//    /**
//     * 生效时间，暂时不上众测服务器
//     */
//    private Integer effective_time;
}
