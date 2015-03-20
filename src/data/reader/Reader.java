package data.reader;

import action.ActionNames;
import action.ActionType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.util.ArrayUtilities;
import proc.BRLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Reader {

    public String[][] readExcelFile(File inputFile, String sheetName, int fromRow, int toRow, int fromColumn, int toColumn) throws InvalidFormatException, IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(OPCPackage.openOrCreate(inputFile));
        XSSFSheet sheet = workbook.getSheet(sheetName);

        String[][] output = new String[toRow - fromRow + 1][toColumn - fromColumn + 1];

        for (int r = fromRow; r < toRow + 1; r++) {
            for (int c = fromColumn; c < toColumn + 1; c++) {
                output[r - fromRow][c - fromColumn] = sheet.getRow(r).getCell(c).getRawValue();
            }
        }
        return output;
    }

    public ArrayList[] readExcelFile(File inputFile, String sheetName, int fromRow, int toRow, int fromColumn, String endCell) throws InvalidFormatException, IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(inputFile));
        XSSFSheet sheet = workbook.getSheet(sheetName);

        ArrayList output[] = new ArrayList[(toRow - fromRow) + 1];

        for (int r = fromRow; r < toRow + 1; r++) {
            output[r - fromRow] = new ArrayList<String>();
            Iterator<Cell> it = sheet.getRow(r).iterator();

            while (it.hasNext()) {
                Cell next = it.next();
                if (next.getColumnIndex() < fromColumn)
                    continue;
                if (next.getStringCellValue().equals(endCell))
                    break;
                else
                    output[r - fromRow].add(next.getStringCellValue());
            }
        }
        return output;
    }

    public static ArrayList[] parseActions(String filePath, String sheetName) {

        ArrayList[] rawActionsList = new ArrayList[0];
        ArrayList[] rawTimesList = new ArrayList[0];
        try {
            rawActionsList = new Reader().readExcelFile(new File(filePath), sheetName, 1, 8, 2, "0.000");
            rawTimesList = new Reader().readExcelFile(new File(filePath), sheetName, 1, 8, 1002, "0.000");
        } catch (Exception e) {
            throw new BRLException(e);
        }

        ArrayList<ActionType>[] actionsList = new ArrayList[rawActionsList.length];

        for (int j = 0; j < actionsList.length; j++) {
            ArrayList actions = rawActionsList[j];
            ArrayList<ActionType> modActions = new ArrayList<ActionType>();
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).equals("1.000"))
                    modActions.add(ActionType.get(ActionNames.pressLever));
                if (actions.get(i).equals("2.000")) {
                    if (
                            i == 0 ||
                            !actions.get(i - 1).equals("2.000") ||
                            (Double.parseDouble((String) rawTimesList[j].get(i)) -
                                Double.parseDouble((String) rawTimesList[j].get(i - 1)) > 1))
                            modActions.add(ActionType.get(ActionNames.enterMagazine));
                }
            }
            actionsList[j] = modActions;
        }
        return actionsList;
    }
}