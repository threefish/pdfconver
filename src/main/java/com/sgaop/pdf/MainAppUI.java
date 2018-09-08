package com.sgaop.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
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

    private PDDocument document = new PDDocument();
    private String filePath;

    public MainAppUI() {
        JFrame jFrame = new JFrame();
        int w = 450, h = 120;
        jFrame.setTitle("PDF拆分工具");
        jFrame.setResizable(false);
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (w / 2));
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (h / 2));
        jFrame.setContentPane(rootPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setBounds(x, y, w, h);
        choseFile.addActionListener((e) -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.showDialog(new JLabel(), "选择PDF文件");
            FileFilter pdfFileFilter = new FileNameExtensionFilter("PDF file", "pdf", "PDF");
            jfc.addChoosableFileFilter(pdfFileFilter);
            jfc.setFileFilter(pdfFileFilter);
            File file = jfc.getSelectedFile();
            if (file != null && file.isFile()) {
                pdfPath.setText(file.getAbsolutePath());
                filePath = file.getParent() + File.separator + file.getName();
                try {
                    document = PDDocument.load(file);
                    pageLable.setText(String.valueOf(document.getNumberOfPages()));
                    startSpilt.setEnabled(true);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(rootPanel, "文件有误！" + e1.getMessage());
                }
            }
        });
        startSpilt.addActionListener((e) -> {
            try {
                int start = Integer.parseInt(startPage.getText());
                int end = Integer.parseInt(endPage.getText());
                PDDocument tempdoc = new PDDocument();
                for (int i = start; i < end; i++) {
                    tempdoc.addPage(document.getPage(i));
                }
                String path = MessageFormat.format("{0}-{1}-{2}.pdf", filePath, start, end);
                tempdoc.save(path);
                JOptionPane.showMessageDialog(rootPanel, "生成成功！" + path);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(rootPanel, "生成失败！" + e1.getMessage());
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
