package com.jovision.jaws.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonDeserializer extends JsonDeserializer<Date>
{
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException
    {
        try
        {
            if(jsonParser==null || jsonParser.getText()==null || "".equals(jsonParser.getText())){
                return null;
            }else{
                return format.parse(jsonParser.getText());
            }

        }catch(Exception e) {
            throw new BusinessException(BusinessErrorEnum.BASE_PARAM_FORMAT);
        }
    }
}