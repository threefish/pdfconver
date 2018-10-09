package com.sgaop.pdf;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author 黄川 huchuc@vip.qq.com
 * @date 2018/10/09 0020
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
