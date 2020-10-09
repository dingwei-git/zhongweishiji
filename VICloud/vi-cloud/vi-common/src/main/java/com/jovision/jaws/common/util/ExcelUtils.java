package com.jovision.jaws.common.util;

import com.huawei.vi.entity.vo.Menu;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    //创建Excel对象
    public static void createExcelFile(Map menuMap, Map sheetNameMap, Map dataMap, HttpServletResponse response) {
        OutputStream out = null;
        try {
            //文档输出
            HSSFWorkbook workbook=createHSSFSheet(menuMap,sheetNameMap,dataMap);
//            out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\excel\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() +".xls");
            out = response.getOutputStream();
            workbook.write(out);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.flush();
                    out.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private static HSSFWorkbook createHSSFSheet(Map menuMap, Map sheetNameMap, Map dataMap){
        //创建工作薄对象
        HSSFWorkbook workbook=new HSSFWorkbook();//这里也可以设置sheet的Name
        for(Object key:sheetNameMap.keySet()){
            List sheetNameList = (List) sheetNameMap.get(key);
            List header = (List) menuMap.get(key);
            List data = (List) dataMap.get(key);
            //创建工作表的行
            for(int j=0;j<sheetNameList.size();j++){
                //创建工作表对象
                HSSFSheet sheet = workbook.createSheet();
                editHeader(header,sheet);
                for(int k=0;k<data.size();k++){
                    HSSFRow row = sheet.createRow(k+1);//设置数据,从第二行开始
                    editData(row,data.get(k),header);
                }
                workbook.setSheetName(j,TraverseObjectUtil.trim(sheetNameList.get(j)));//设置sheet的Name
            }
        }

        return workbook;
    }

    private static void editHeader(List<Menu> header,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);//设置第一行，从零开始;
        for(int i=0;i<header.size();i++){
            row.createCell(i).setCellValue(header.get(i).getTitle());//第一行第一列为日期
        }
    }

    private static void editData(HSSFRow row,Object data,List<Menu> header){
        for(int i=0;i<header.size();i++){
            String value = TraverseObjectUtil.trim(TraverseObjectUtil.getFieldValueByName(header.get(i).getKey(),data));
            row.createCell(i).setCellValue(value);//第一行第一列为日期
        }
    }

}
