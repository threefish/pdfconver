package com.sgaop.pdf;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 黄川
 * 创建时间: 2017/11/8  14:34
 * 描述此类：
 */
public class PdfFileFilter extends FileFilter {
    private static final String SUFFIX = ".pdf";

    @Override
    public boolean accept(File f) {
        if (f.getAbsolutePath().toLowerCase().endsWith(SUFFIX) || f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "请选择 .pdf 格式文件";
    }
}
