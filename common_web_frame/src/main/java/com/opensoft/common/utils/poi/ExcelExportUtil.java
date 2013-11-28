package com.opensoft.common.utils.poi;

import org.apache.poi.hssf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Description:
 * User: KangWei
 * Date: 11-11-3
 */
public class ExcelExportUtil {

    //  定制日期格式
    private static String DATE_FORMAT = " m/d/yy ";  //  "m/d/yy h:mm"

    //  定制浮点数格式
    private static String NUMBER_FORMAT = " #,##0.00 ";

    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    private HSSFRow row;

    /**
     * 初始化Excel
     *
     */
    public ExcelExportUtil() {
        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet();
    }

    /**
     * 导出Excel文件
     *
     * @param outputStream outputStream
     * @throws Exception Exception
     */
    public void exportXLS(OutputStream outputStream) throws Exception {
        try {
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new Exception(" 生成导出Excel文件出错! ", e);
        } catch (IOException e) {
            throw new Exception(" 写入Excel文件出错! ", e);
        }

    }

    /**
     * 增加一行
     *
     * @param index 行号
     */
    public void createRow(int index) {
        this.row = this.sheet.createRow(index);
    }

    /**
     * 设置单元格
     *
     * @param index 列号
     * @param value 单元格填充值
     */
    public void setCell(int index, String value) {
        HSSFCell cell = this.row.createCell(index);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    /**
     * 设置单元格
     *
     * @param index 列号
     * @param value 单元格填充值
     */
    public void setCell(int index, Calendar value) {
        HSSFCell cell = this.row.createCell(index);
        cell.setCellValue(value.getTime());
        HSSFCellStyle cellStyle = workbook.createCellStyle();  //  建立新的cell样式
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));  //  设置cell样式为定制的日期格式
        cell.setCellStyle(cellStyle);  //  设置该cell日期的显示格式
    }

    /**
     * 设置单元格
     *
     * @param index 列号
     * @param value 单元格填充值
     */
    public void setCell(int index, int value) {
        HSSFCell cell = this.row.createCell(index);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    /**
     * 设置单元格
     *
     * @param index 列号
     * @param value 单元格填充值
     */
    public void setCell(int index, double value) {
        HSSFCell cell = this.row.createCell(index);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        HSSFCellStyle cellStyle = workbook.createCellStyle();  //  建立新的cell样式
        HSSFDataFormat format = workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT));  //  设置cell样式为定制的浮点数格式
        cell.setCellStyle(cellStyle);  //  设置该cell浮点数的显示格式
    }

}
