package com.jovision.jaws.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ListDateJsonDeserializer extends JsonDeserializer<List<Date>>
{
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public List<Date> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    {
        try {
            List array = jsonParser.readValueAs(List.class);

            if(jsonParser == null || CollectionUtil.isEmpty(array)){
                throw new BusinessException(BusinessErrorEnum.BASE_PARAM_FORMAT);
            }else{
                return (List<Date>) array.stream()
                        .map(dateJson -> formatParse(String.valueOf(dateJson)))
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
            throw new BusinessException(BusinessErrorEnum.BASE_PARAM_FORMAT);
        }
    }

    public Date formatParse(String dateStr) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new BusinessException(BusinessErrorEnum.BASE_PARAM_FORMAT);
        }
    }
}