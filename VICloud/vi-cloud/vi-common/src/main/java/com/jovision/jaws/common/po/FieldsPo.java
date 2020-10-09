package com.jovision.jaws.common.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : fengyefeiluo
 * @ClassName FieldsPo
 * @Version 1.0
 * @Date : 2020/1/20 15:18
 * @UpdateDate : 2020/1/20 15:18
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldsPo {
    private List<KeyValuePo> fields;
}
