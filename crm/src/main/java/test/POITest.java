package test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @Description TODO
 * @Date 2022/1/18 21:06
 * @Version 1.0
 */
public class POITest {

    public static void main(String[] args) throws IOException {
        readExcel();
        writeExcel();
    }

    private static void writeExcel() throws IOException {

        //创建工作簿对象
        HSSFWorkbook workbookXls = new HSSFWorkbook();
        XSSFWorkbook workbookXlsx = new XSSFWorkbook();

        //根据工作簿对象创建页码对象
        HSSFSheet sheet1 = workbookXls.createSheet("测试...xls");
        XSSFSheet sheet2 = workbookXlsx.createSheet("测试...xlsx");

        //根据页码对象创建行对象
        HSSFRow r1 = sheet1.createRow(0);
        XSSFRow rr1 = sheet2.createRow(0);

        //根据行对象创建单元格对象
        //赋值操作
        r1.createCell(0).setCellValue("aaa");
        r1.createCell(1).setCellValue("bbb");
        r1.createCell(2).setCellValue("ccc");

        rr1.createCell(0).setCellValue("aaa");
        rr1.createCell(1).setCellValue("bbb");
        rr1.createCell(2).setCellValue("ccc");

        //写入操作
        workbookXls.write(new FileOutputStream(new File("/Users/mingxuan/Desktop/aaa.xls")));
        workbookXlsx.write(new FileOutputStream(new File("/Users/mingxuan/Desktop/bbb.xlsx")));

    }

    private static void readExcel() throws IOException {

        //通过输入流对象,读取xls,低版本Excel文件
        InputStream inXls = new FileInputStream(new File("/Users/mingxuan/Desktop/Activity.xls"));
        //通过输入流对象,读取xlsx,高版本Excel文件
        InputStream inXlsx = new FileInputStream(new File("/Users/mingxuan/Desktop/abc.xlsx"));


        //低版本的工作簿对象
        HSSFWorkbook workbookXls = new HSSFWorkbook(inXls);

        //读取页码对象
        HSSFSheet sheet = workbookXls.getSheetAt(0);

        System.out.println("---------低版本---------");
        //获取行对象
        HSSFRow r1 = sheet.getRow(0);
        String c1 = r1.getCell(0).getStringCellValue();
        String c2 = r1.getCell(1).getStringCellValue();
        String c3 = r1.getCell(2).getStringCellValue();
        HSSFRow r2 = sheet.getRow(1);
        String c21 = r2.getCell(0).getStringCellValue();
        String c22 = r2.getCell(1).getStringCellValue();
        String c23 = r2.getCell(2).getStringCellValue();
        HSSFRow r3 = sheet.getRow(2);
        String c31 = r3.getCell(0).getStringCellValue();
        String c32 = r3.getCell(1).getStringCellValue();
        String c33 = r3.getCell(2).getStringCellValue();

        System.out.println("第一行: "+c1 + " " + c2+" "+c3);
        System.out.println("第二行: "+c21 + " " + c22+" "+c23);
        System.out.println("第三行: "+c31 + " " + c32+" "+c33);

        System.out.println("---------高版本---------");

        //高版本的工作簿对象
        XSSFWorkbook workbookXlsx = new XSSFWorkbook(inXlsx);

        XSSFSheet sheet1 = workbookXlsx.getSheetAt(0);

        XSSFRow r11 = sheet1.getRow(0);
        String c41 = r11.getCell(0).getStringCellValue();
        String c42 = r11.getCell(1).getStringCellValue();
        String c43 = r11.getCell(2).getStringCellValue();
        XSSFRow r22 = sheet1.getRow(1);
        String c421 = r22.getCell(0).getStringCellValue();
        String c422 = r22.getCell(1).getStringCellValue();
        String c423 = r22.getCell(2).getStringCellValue();

        System.out.println("第一行: "+c41 + " " + c42+" "+c43);
        System.out.println("第二行: "+c421 + " " + c422+" "+c423);
    }

}
