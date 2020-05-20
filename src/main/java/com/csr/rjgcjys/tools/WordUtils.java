package com.csr.rjgcjys.tools;

public class WordUtils {

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            } // end if
        } // end if
        return filename;
    }

    /**
     * 获取不带扩展名的文件名
     * @param filename 文件名，含后缀名。 file.getOriginalFilename()
     * @return 不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
            if ((filename != null) && (filename.length() > 0)) {
                int dot = filename.lastIndexOf('.');
                if ((dot >-1) && (dot < (filename.length()))) {
                    return filename.substring(0, dot);
                }
            }
        return filename;
    }




}
