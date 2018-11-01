package com.sgaop.test;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Pdf2word {


    public static void main(String[] args) {
        String pdfFile = "C:/Users/HC/Desktop/四川大学管理信息标准.pdf";
        File file = new File(pdfFile);
        try {
            PDFParser parser = new PDFParser(new org.apache.pdfbox.io.RandomAccessFile(file, "rw"));
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDDocument pdDoc = new PDDocument(cosDoc);
            int pagenumber = pdDoc.getNumberOfPages();
            pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
            String fileName = pdfFile + "_1.doc";
            FileOutputStream fos = new FileOutputStream(fileName);
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            PDFTextStripper stripper = new PDFTextStripper();
            // 排序
            stripper.setSortByPosition(true);
            // 设置转换的开始页
            stripper.setStartPage(1);
            // 设置转换的结束页
            stripper.setEndPage(pagenumber);
            stripper.writeText(pdDoc, writer);
            writer.close();
            pdDoc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}