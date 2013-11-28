/**
 * ClassName: POIWork
 * CopyRight: TalkWeb
 * Date: 12-9-11
 * Version: 1.0
 */
package com.opensoft.common.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Description : excel，word的操作工具类
 * User : 康维
 */
public class POIWork {
    private String filePath;    //文件路径
    private HSSFWorkbook workbook;  //工作表

    public POIWork(String filePath) throws IOException {
        workbook = new HSSFWorkbook(new FileInputStream(filePath));
    }

    /**
     * @param sheet 子工作表
     * @param row   行
     * @param cell  列
     * @return 单元格字符串值
     * @throws java.io.IOException 异常
     */
    public String getStringValue(int sheet, int row, int cell) throws IOException {
        return getCell(sheet, row, cell).getStringCellValue();
    }

    /**
     * @param sheet 子工作表
     * @param row   行
     * @param cell  列
     * @return 单元格boolean值
     * @throws java.io.IOException 异常
     */
    public Boolean getBooleanValue(int sheet, int row, int cell) throws IOException {
        return getCell(sheet, row, cell).getBooleanCellValue();
    }

    /**
     * @param sheet 子工作表
     * @param row   行
     * @param cell  列
     * @return 单元格数值
     * @throws java.io.IOException 异常
     */
    public Double getNumericValue(int sheet, int row, int cell) throws IOException {
        return getCell(sheet, row, cell).getNumericCellValue();
    }

    /**
     * @param sheet 子工作表
     * @param row   行
     * @param cell  列
     * @return 单元格日期值
     * @throws java.io.IOException 异常
     */
    public Date getDateValue(int sheet, int row, int cell) throws IOException {
        return getCell(sheet, row, cell).getDateCellValue();
    }

    /**
     * 读取工作表的列
     *
     * @param sheet 子工作表
     * @param row   行
     * @param cell  列
     */
    private HSSFCell getCell(int sheet, int row, int cell) {
        HSSFSheet sheetAt = workbook.getSheetAt(sheet);
        HSSFRow hssfRow = sheetAt.getRow(row);

        return hssfRow.getCell(cell);
    }

    /**
     * 获取当前工作表的行数
     *
     * @param sheet 子工作表
     * @return 行数
     */
    public int getRowNum(int sheet) {
        HSSFSheet sheetAt = workbook.getSheetAt(sheet);
        return sheetAt.getLastRowNum();
    }

    /**
     * 获取当前行的列数
     *
     * @param sheet 子工作表
     * @param row   行数
     * @return 列数
     */
    public int getCellNum(int sheet, int row) {
        HSSFSheet sheetAt = workbook.getSheetAt(sheet);
        return sheetAt.getRow(row).getPhysicalNumberOfCells();
    }

    /**
     * 根据路径读取工作本
     *
     * @param filePath 文件地址
     * @return 工作表
     * @throws java.io.IOException 异常
     */
    private static HSSFWorkbook getWorkBook(String filePath) throws IOException {
//        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(filePath));
        return new HSSFWorkbook(new FileInputStream(filePath));
    }


    //////////////////////////////////get/set///////////////////////////////////////////
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }
}
