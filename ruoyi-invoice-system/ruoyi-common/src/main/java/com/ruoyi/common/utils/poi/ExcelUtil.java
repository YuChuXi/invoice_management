package com.ruoyi.common.utils.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 实体类对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 导出Excel
     */
    public byte[] exportExcel(List<T> list, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet(sheetName);
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            Field[] fields = clazz.getDeclaredFields();
            
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
            }
            
            // 填充数据
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                T item = list.get(i);
                
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    Object value = field.get(item);
                    Cell cell = row.createCell(j);
                    
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
            
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException | IllegalAccessException e) {
            log.error("导出Excel异常", e);
            return new byte[0];
        }
    }
}
