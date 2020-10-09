
package com.jovision.jaws.common.util;

import com.huawei.utils.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Properties;

/**
 * XML工具类
 *
 * @version 1.0, 2018年6月20日
 * @since 2018-06-20
 */
public final class XmlUtil {
    private static XmlUtil instance = new XmlUtil();

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

    private XmlUtil() {
    }

    /**
     * 解析xml,转换成对象
     *
     * @param path 文件路径
     * @param valueType 类名
     * @param <TT> 泛型类
     * @return T
     */
    public static <TT> TT xmlToObject(String path, Class<TT> valueType) {
        TT bean = null;
        if (CommonUtil.isNull(path)) {
            LOGGER.info("the xml file path is not null");
            return null;
        }
        try (InputStream wasXmlInput = PropertiesUtil.getInputStreamByFile(path)) {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(wasXmlInput);
            JAXBContext context = JAXBContext.newInstance(valueType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            bean = (TT) unmarshaller.unmarshal(xsr);
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException ");
        } catch (JAXBException e) {
            LOGGER.error("JAXBException {} ", e.getMessage());
        } catch (XMLStreamException e) {
            LOGGER.error("XMLStreamException {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException {}", e.getMessage());
        }
        return bean;
    }

    /**
     * xml转化为bean>
     *
     * @param path 路径
     * @param valueType 类型
     * @param encoder 编码
     * @param <TT> 泛型
     * @return T
     */
    public static <TT> TT xmlToObject(String path, Class<TT> valueType, String encoder) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        TT tt = null;
        try {
            fis = FileUtils.openInputStream(FileUtils.getFile(path));
            isr = new InputStreamReader(fis, encoder);
            br = new BufferedReader(isr);
            JAXBContext context = JAXBContext.newInstance(valueType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            tt = (TT) unmarshaller.unmarshal(br);
        } catch (IOException e) {
            LOGGER.info("IOException Exception:{} ", e.getMessage());
        } catch (JAXBException e) {
            LOGGER.info("JAXBException Exception:{} ", e.getMessage());
        } finally {
            UtilMethod.closeFileStreamNotThrow(br);
            UtilMethod.closeFileStreamNotThrow(isr);
            UtilMethod.closeFileStreamNotThrow(fis);
        }
        return tt;
    }
}