package com.itcag.docanalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ISDTContent;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class MSWord {

    public static ArrayList<String> parse(String filePath) throws Exception {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        Path msWordPath = Paths.get(filePath);
        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(msWordPath))) {
            
            retVal.addAll(parseHeaders(document.getHeaderList()));
            
            for (IBodyElement element : document.getBodyElements()) {
                retVal.addAll(parseBodyElement(element));
            }
            
        }        
        
        return retVal;
        
    }
    
    private static ArrayList<String> parseHeaders(List<XWPFHeader> headers) throws Exception {

        ArrayList<String> retVal = new ArrayList<>();
        
        for (XWPFHeader header : headers) {
            for (IBodyElement element : header.getBodyElements()) {
                retVal.addAll(parseBodyElement(element));
            }
        }
    
        return retVal;
        
    }

    private static ArrayList<String> parseTable(XWPFTable table) throws Exception {

        ArrayList<String> retVal = new ArrayList<>();
        
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                for (IBodyElement element : cell.getBodyElements()) {
                    retVal.addAll(parseBodyElement(element));
                }
            }
        }
    
        return retVal;
        
    }
    
    private static ArrayList<String> parseBodyElement(IBodyElement element) throws Exception {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        if (null != element.getElementType()) switch (element.getElementType()) {
            case PARAGRAPH:{
                XWPFParagraph paragraph = (XWPFParagraph) element;
                String text = paragraph.getText().trim();
                if (!text.isEmpty()) retVal.add(text);
                    break;
                }
            case CONTENTCONTROL:{
                XWPFSDT paragraph = (XWPFSDT) element;
                ISDTContent content = paragraph.getContent();
                String text = content.getText();
                if (!text.isEmpty()) retVal.add(text);
                    break;
                }
            case TABLE:
                XWPFTable table = (XWPFTable) element;
                retVal.addAll(parseTable(table));
                break;
            default:
                break;
        }
    
        return retVal;

    }
    
}
