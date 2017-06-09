/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsxparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tree.Product;
import tree.Ressource;
import tree.Process;

/**
 * Provides the Method to parse an XLSX File
 *
 * @author billi
 * @version 1.1 25.01.2017 (DD.MM.YYYY)
 */
public class XLSXParser
{

    private final LinkedList<Process> process = new LinkedList();
    private final LinkedList<Ressource> ressource = new LinkedList();
    private final LinkedList<Product> product = new LinkedList();
    private static final int HCRGBPROCESS = -311893721, HCRGBRESSOURCE = 2035135491,
            HCRGBPRODUCT = -720952531;

    /**
     * Parses the file which is given in the following Parameter:
     *
     * @param fileplath path to the file as String
     * @param debug if true Steps are printed in the Command Line
     */
    public void parse(String fileplath, boolean debug)
    {
        try
        {
            File myFile = new File(fileplath);
            FileInputStream fis = new FileInputStream(myFile);

            /* Öffnet Tabelle1 */
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /* Erzeugt ein Zeiger für die Zeilen */
            Iterator<Row> rowIterator = mySheet.iterator();
            Row row = rowIterator.next();

            /* Erzeugt ein Zeiger für die Zellen */
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;

            /* Liest alle PPR Elemente aus der ersten Zeile ein */
            while (cellIterator.hasNext())
            {

                cell = cellIterator.next();

                /* Leerzeichen entfernen */
                String substring = cell.getStringCellValue();
                while (substring.endsWith(" "))
                {
                    substring = substring.substring(0, substring.length() - 1);
                }
                while (substring.startsWith(" "))
                {
                    substring = substring.substring(1, substring.length());
                }
                cell.setCellValue(substring);

                /* Prüft das keine leere Zelle dazwischen ist */
                if (cell.getCellStyle() != null)
                {
                    if (cell.getCellStyle().getFillForegroundColorColor() != null)
                    {
                        /* Anahnd der Hintergrundfarbe wird erkannt um welches PPR Element es sich handelt*/
                        switch (cell.getCellStyle().getFillForegroundColorColor().hashCode())
                        {
                            case HCRGBPROCESS:

                                /* String auslesen und Prozess erstellen */
                                process.add(new Process(cell.getStringCellValue()));
                                if (debug)
                                {
                                    System.out.println(cell.getStringCellValue() + " wurden zu den Prozessen hinzugefügt");
                                }
                                break;

                            case HCRGBRESSOURCE:

                                ressource.add(new Ressource(cell.getStringCellValue()));
                                if (debug)
                                {
                                    System.out.println(cell.getStringCellValue() + " wurde zu den Ressourcen hinzugefügt");
                                }
                                break;

                            case HCRGBPRODUCT:

                                product.add(new Product(cell.getStringCellValue()));
                                if (debug)
                                {
                                    System.out.println(cell.getStringCellValue() + " wirde zu den Produkten hinzugefügt");
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }

            }

            /* Liest alle PPR Elemente aus der ersten Reihe ein und vergleicht ob sie schon abgespeichert worden sind. */
 /*    Wenn nicht werden sie den jeweiligen Listen hinzugefügt */
            while (rowIterator.hasNext())
            {

                /*Legt den Zeiger auf die erste Reihe und geht Zeilenweise durch */
                row = rowIterator.next();
                cellIterator = row.cellIterator();
                cell = cellIterator.next();

                /* Leerzeichen entfernen */
                String substring = cell.getStringCellValue();
                while (substring.endsWith(" "))
                {
                    substring = substring.substring(0, substring.length() - 1);
                }
                while (substring.startsWith(" "))
                {
                    substring = substring.substring(1, substring.length());
                }
                cell.setCellValue(substring);

                /* Prüfung für leere Zellen ohne Hintergrundfarbe*/
                if (cell.getCellStyle() != null)
                {
                    if (cell.getCellStyle().getFillForegroundColorColor() != null)
                    {
                        /* Aufgrund der Hintergrundfarbe wird erkannt um welches PPR ELement es sich handelt */
                        switch (cell.getCellStyle().getFillForegroundColorColor().hashCode())
                        {
                            case HCRGBPROCESS:

                                boolean a = true;
                                /* Prüfen ob das Element noch nicht in der Ersten Zeile vorgekommen ist */
                                for (Process p : process)
                                {
                                    if (cell.getStringCellValue().equalsIgnoreCase(p.getName()))
                                    {
                                        a = false;
                                    }
                                }
                                /* Wenn nicht hinzufügen */
                                if (a)
                                {
                                    process.add(new Process(cell.getStringCellValue()));
                                    if (debug)
                                    {
                                        System.out.println(cell.getStringCellValue() + " wurden zu den Prozessen hinzugefügt");
                                    }
                                }
                                break;

                            case HCRGBRESSOURCE:

                                a = true;
                                for (Ressource r : ressource)
                                {

                                    if (cell.getStringCellValue().equalsIgnoreCase(r.getName()))
                                    {
                                        a = false;
                                    }
                                }
                                if (a)
                                {
                                    ressource.add(new Ressource(cell.getStringCellValue()));
                                    if (debug)
                                    {
                                        System.out.println(cell.getStringCellValue() + " wurde zu den Ressourcen hinzugefügt");
                                    }
                                }
                                break;

                            case HCRGBPRODUCT:

                                a = true;
                                for (Product p : product)
                                {
                                    if (cell.getStringCellValue().equalsIgnoreCase(p.getName()))
                                    {
                                        a = false;
                                    }
                                }
                                if (a)
                                {
                                    product.add(new Product(cell.getStringCellValue()));
                                    if (debug)
                                    {
                                        System.out.println(cell.getStringCellValue() + " wirde zu den Produkten hinzugefügt");
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }

            }

            /* Zeiger zum Start auf die Erste Zeile und Reihe legen */
            rowIterator = mySheet.rowIterator();
            row = rowIterator.next();
            cellIterator = row.cellIterator();

            /*  Zeilenweise alle Anhängigkeiten einsammeln und entsprechend verknüpfen */
            while (rowIterator.hasNext())
            {
                row = rowIterator.next();
                cellIterator = row.cellIterator();
                cell = cellIterator.next();

                while (cellIterator.hasNext())
                {
                    cell = cellIterator.next();

                    /* Prüfen ob Zelle befüllt ist */
                    if (!cell.getStringCellValue().isEmpty())
                    {
                        /* Prüfen um welches PPR Element sich in dieser Zeile handelt */
                        switch (row.getCell(0).getCellStyle().getFillForegroundColorColor().hashCode())
                        {
                            case HCRGBPROCESS:

                                /* zugehörigen Prozess anhand des Strings aus der Liste suchen */
                                for (Process p : process)
                                {
                                    if (row.getCell(0).getStringCellValue().equalsIgnoreCase(p.getName()))
                                    {

                                        /* Prüfen um welches PPR Element es sich in dieser Reihe handelt */
                                        switch (mySheet.getRow(0).getCell(cell.getColumnIndex()).getCellStyle().getFillForegroundColorColor().hashCode())
                                        {
                                            case HCRGBPROCESS:

                                                /* zugehörigen Prozess finden */
                                                for (Process p2 : process)
                                                {
                                                    if (p2.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        /* verbindung dem PPR Element aus der Zeile hinzufügen */
                                                        p.addConnection(p2, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBRESSOURCE:

                                                for (Ressource r : ressource)
                                                {
                                                    if (r.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        p.addConnection(r, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBPRODUCT:

                                                for (Product po : product)
                                                {
                                                    if (po.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        p.addConnection(po, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                                break;

                            case HCRGBRESSOURCE:

                                for (Ressource r : ressource)
                                {
                                    if (row.getCell(0).getStringCellValue().equalsIgnoreCase(r.getName()))
                                    {
                                        switch (mySheet.getRow(0).getCell(cell.getColumnIndex()).getCellStyle().getFillForegroundColorColor().hashCode())
                                        {
                                            case HCRGBPROCESS:

                                                for (Process p2 : process)
                                                {
                                                    if (p2.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        r.addConnection(p2, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBRESSOURCE:

                                                for (Ressource r2 : ressource)
                                                {
                                                    if (r2.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        r.addConnection(r2, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBPRODUCT:

                                                for (Product po : product)
                                                {
                                                    if (po.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        r.addConnection(po, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                                break;

                            case HCRGBPRODUCT:

                                for (Product p : product)
                                {
                                    if (row.getCell(0).getStringCellValue().equalsIgnoreCase(p.getName()))
                                    {
                                        switch (mySheet.getRow(0).getCell(cell.getColumnIndex()).getCellStyle().getFillForegroundColorColor().hashCode())
                                        {
                                            case HCRGBPROCESS:

                                                for (Process p2 : process)
                                                {
                                                    if (p2.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        p.addConnection(p2, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBRESSOURCE:

                                                for (Ressource r : ressource)
                                                {
                                                    if (r.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        p.addConnection(r, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            case HCRGBPRODUCT:

                                                for (Product po : product)
                                                {
                                                    if (po.getName().equalsIgnoreCase(mySheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue()))
                                                    {
                                                        p.addConnection(po, cell.getStringCellValue());
                                                        if (debug)
                                                        {
                                                            System.out.println(row.getCell(0).getStringCellValue() + " wurde eine Verbindung zu "
                                                                    + mySheet.getRow(0).getCell(cell.getColumnIndex()) + " mit der Abhängigkeit " + cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        } catch (IOException ex)
        {
            Logger.getLogger(XLSXParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString()
    {
        /* String Builder zur besseren Performance bei größeren String ketten */
        StringBuilder collection = new StringBuilder();
        for (Process p : process)
        {
            collection.append(p.toString());
        }
        for (Ressource r : ressource)
        {
            collection.append(r.toString());
        }
        for (Product pr : product)
        {
            collection.append(pr.toString());
        }
        return collection.toString();
    }

}
