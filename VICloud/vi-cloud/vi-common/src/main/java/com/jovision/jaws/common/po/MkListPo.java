package com.jovision.jaws.common.po;

import com.jovision.jaws.common.vo.MasterKeyVo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author : fengyefeiluo
 * @ClassName MkListPo
 * @Version 1.0
 * @Date : 2020/1/20 16:54
 * @UpdateDate : 2020/1/20 16:54
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
public class MkListPo {
    private List<MasterKeyVo> master_key_llst;
}
