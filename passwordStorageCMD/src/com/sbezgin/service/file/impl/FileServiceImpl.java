package com.sbezgin.service.file.impl;

import com.sbezgin.service.file.FileService;

import java.io.*;
import java.util.Arrays;

public class FileServiceImpl implements FileService {
    private static final String FILE_FOLDER = "c:/tmp";
    private static final String FILE_NAME = "test.pass";
    private static final String FILE_PATH = FILE_FOLDER + "/" + FILE_NAME;

    @Override
    public InputStream getCurrentFileInputStream() throws FileNotFoundException {
        return new FileInputStream(FILE_PATH);
    }

    @Override
    public void saveDate(byte[] data) throws IOException {
        File dir = new File(FILE_FOLDER);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                return name.matches("^" + FILE_NAME + ".*$");
            }
        });

        Arrays.sort(files);

        if (files.length >= 4) {
            File file = files[1];
            if (!file.delete()) {
                throw new RuntimeException("Cannot delete file: " + file.getCanonicalPath());
            }
        }

        String nextPrefix = getNextPrefix(files);
        File currentFile = new File(FILE_PATH);
        if (currentFile.exists()) {
            if (!currentFile.renameTo(new File(FILE_PATH + "." + nextPrefix))) {
                throw new RuntimeException("Cannot rename file: " + currentFile.getCanonicalPath());
            }
        }

        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(FILE_PATH));
        outStream.write(data);
        outStream.flush();
        outStream.close();
    }

    private String getNextPrefix(File[] files) {
        Integer biggest = 0;
        for (File file : files) {
            String name = file.getName();
            String extention = name.substring(name.lastIndexOf('.'));

            Integer number = -1;
            try {
                number = Integer.parseInt(extention.substring(4));
                if (biggest < number) {
                    biggest = number;
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        return "000" + (biggest + 1);
    }
}
