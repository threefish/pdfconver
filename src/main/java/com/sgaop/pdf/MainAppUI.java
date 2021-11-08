package com.sgaop.pdf;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author 黄川 huchuc@vip.qq.com
 * @date 2018/9/8 0008
 */
public class MainAppUI {

    private JTextField pdfPath;
    private JButton choseFile;
    private JLabel pageLable;
    private JTextField startPage;
    private JTextField endPage;
    private JButton startSpilt;
    private JPanel rootPanel;
    private JButton converToImg;
    private JTextField dpi;

    private PDDocument document = new PDDocument();
    private String filePath;

    public MainAppUI() {
        JFrame jFrame = new JFrame();
        int w = 500, h = 160;
        jFrame.setTitle("PDF拆分工具( huchuc@vip.qq.com )");
        jFrame.setResizable(false);
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (w / 2));
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (h / 2));
        jFrame.setContentPane(rootPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setBounds(x, y, w, h);
        choseFile.addActionListener((event) -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter(new PdfFileFilter());
            jfc.showDialog(new JLabel(), "选择PDF文件");
            File file = jfc.getSelectedFile();
            if (file != null && file.isFile()) {
                pdfPath.setText(file.getAbsolutePath());
                filePath = file.getParent() + File.separator + file.getName();
                try {
                    document = PDDocument.load(file);
                    pageLable.setText(String.valueOf(document.getNumberOfPages()));
                    endPage.setText(String.valueOf(document.getNumberOfPages()));
                    startSpilt.setEnabled(true);
                    converToImg.setEnabled(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(rootPanel, "文件有误！" + e.getMessage());
                }
            }
        });
        startSpilt.addActionListener((event) -> {
            try (PDDocument tempdoc = new PDDocument()) {
                int start = Integer.parseInt(startPage.getText());
                int end = Integer.parseInt(endPage.getText());
                if (start < 1) {
                    throw new IllegalArgumentException("开始页码不能小于1");
                }
                if (end > document.getNumberOfPages()) {
                    throw new IllegalArgumentException("结束页码不能大于" + document.getNumberOfPages());
                }
                for (int i = (start - 1); i < end; i++) {
                    tempdoc.addPage(document.getPage(i));
                }
                String path = MessageFormat.format("{0}-{1}-{2}.pdf", filePath, start, end);
                tempdoc.save(path);
                JOptionPane.showMessageDialog(rootPanel, "生成成功！" + path);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPanel, "生成失败！" + e.getMessage(), "错误警告", JOptionPane.ERROR_MESSAGE);
            }
        });
        converToImg.addActionListener((event) -> {
            try {
                int start = Integer.parseInt(startPage.getText());
                int end = Integer.parseInt(endPage.getText());
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                //存储pdf中每一页BufferedImage
                java.util.List<BufferedImage> bufferedImageList = new ArrayList();
                for (int i = start; i < end; i++) {
                    //注意此处的参数100可以调整，值越大图片越清晰
                    BufferedImage img = pdfRenderer.renderImageWithDPI(i, Integer.parseInt(dpi.getText()), ImageType.RGB);
                    bufferedImageList.add(img);
                }
                int heightTotal = 0;
                for (int j = 0; j < bufferedImageList.size(); j++) {
                    heightTotal += bufferedImageList.get(j).getHeight();
                }
                int heightCurr = 0;
                BufferedImage concatImage = new BufferedImage(bufferedImageList.get(0).getWidth(), heightTotal, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = concatImage.createGraphics();
                for (int j = 0; j < bufferedImageList.size(); j++) {
                    g2d.drawImage(bufferedImageList.get(j), 0, heightCurr, null);
                    heightCurr += bufferedImageList.get(j).getHeight();
                }
                g2d.dispose();
                String path = MessageFormat.format("{0}-{1}-{2}.jpg", filePath, start, end);
                FileOutputStream out = new FileOutputStream(path);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(concatImage);
                out.close();
                JOptionPane.showMessageDialog(rootPanel, "生成成功！" + path);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(rootPanel, "生成失败！" + e.getMessage(), "错误警告", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        initTheme();
        new MainAppUI();
    }

    private static void initTheme() {
        try {
            Font fnt = new Font("Microsoft YaHei UI", Font.PLAIN, 14);
            FontUIResource fontRes = new FontUIResource(fnt);
            for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    UIManager.put(key, fontRes);
                }
            }
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
