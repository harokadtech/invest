package com.harokad;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pl.hofman.projectsGmailApi.Project;

public class ReadDesjHistExcel {

    public static void main(String[] args) throws ParseException {
        String desjAccounts = "5KVXEZ7,5KVXER7,5KVXEY9";
        long fromTime = Project.parseTimeMillis("April 30, 2023 00:30 AM EDT");
        String[] desjAccountsArray = desjAccounts.split(",");
        StringBuilder strOutput = new StringBuilder();
        for (String account : desjAccountsArray) {
            try {
                // Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook workbook = new XSSFWorkbook(
                        new FileInputStream(new File("C:\\Users\\aharo\\Downloads\\Historique-" + account + ".xlsx")));

                // Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(0);
                // Iterate through each rows one by one
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
                rowIterator.next(); // Sti
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell cell = row.getCell(0);
                    String type = row.getCell(2).getStringCellValue();
                    double qty = row.getCell(7).getNumericCellValue();
                    if (type.equals("ACHAT")) {
                        type = "Achat";
                    } else if (type.equals("VENTE")) {
                        type = "Vente";
                        qty = Math.abs(qty);
                    } else {
                        continue;
                    }
                    String dateCell = row.getCell(0).getStringCellValue();
                    Date date = DATE_FORMAT.parse(dateCell);
                    long time = date.getTime();
                    if (time < fromTime) {
                        continue;
                    }
                    String name = row.getCell(4).getStringCellValue().replace("-C", "");
                    name = rightPad(name, 6, " ");
                    double price = row.getCell(8).getNumericCellValue();
                    //double total = row.getCell(11).getNumericCellValue();
                    //total = Math.abs(total);
                    String priceFormated = String.format("%.2f", price);
                    String transDate = Project.S_DATE_FORMAT.format(time);
                    String transLine = MessageFormat.format(TEMPLATE_LINE, type, name, qty, priceFormated, account,
                            "0,00", transDate);
                    //System.out.println(transLine);
                    strOutput.append(transLine);
                    strOutput.append(" \n");
                }
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            GmailInvestExtract.writeStringtoFile(GmailInvestExtract.DESJ_STOCK_INPUT_FILE, strOutput.toString());
          } catch (IOException e) {
            e.printStackTrace();
          }
        System.out.println(strOutput.toString());
    }

    public static String TEMPLATE_LINE = "000009048000	{4}	Exécuté	{0}	{2}	{1}	0,00	{3}	{3}	{5}	{6}";

    public static String TEMPLATE = """
            {0}__Account: {6}
            Type: Limit {1}
            Symbol: {2}
            Shares: {3}
            Average price: ${4}

            Total value: $0.0

            Time: {5}
                """;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /*
     * while (cellIterator.hasNext()) {
     * Cell cell = cellIterator.next();
     * // Check the cell type and format accordingly
     * switch (cell.getCellType()) {
     * case STRING:
     * System.out.print(cell.getStringCellValue() + "\t");
     * break;
     * case NUMERIC:
     * System.out.print(cell.getNumericCellValue() + "\t");
     * break;
     * default:
     * System.out.print(cell.getStringCellValue() + "t");
     * }
     * }
     */

     public static String rightPad(String input, int length, String padStr) {

        if(input == null || padStr == null){
          return null;
        }
      
        if(input.length() >= length){
          return input;
        }
      
        int padLength = length - input.length();
      
        StringBuilder paddedString = new StringBuilder();
        paddedString.append(input);
        paddedString.append(padStr.repeat(padLength));
      
        return paddedString.toString();
      }
}
