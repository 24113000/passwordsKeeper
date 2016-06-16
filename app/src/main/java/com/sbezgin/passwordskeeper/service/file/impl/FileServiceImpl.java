package com.sbezgin.passwordskeeper.service.file.impl;

import com.sbezgin.passwordskeeper.service.file.FileService;

import java.io.*;
import java.util.Arrays;

public class FileServiceImpl implements FileService {
    private static final String FILE_NAME = "encrypted.passwords";

    private final String filePath;
    private final String folder;

    public FileServiceImpl(String folder) {
        this.filePath = folder + "/" + FILE_NAME;
        this.folder = folder;
    }

    @Override
    public byte[] getCurrentFileBytes() throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(filePath);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
            return ous.toByteArray();
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {}

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {}
        }
    }

    @Override
    public InputStream getCurrentFileInputStream() throws FileNotFoundException {
        return new FileInputStream(filePath);
    }

    @Override
    public void saveDate(byte[] data) throws IOException {
        File dir = new File(folder);
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
        File currentFile = new File(filePath);
        if (currentFile.exists()) {
            if (!currentFile.renameTo(new File(filePath + "." + nextPrefix))) {
                throw new RuntimeException("Cannot rename file: " + currentFile.getCanonicalPath());
            }
        }

        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(filePath));
        outStream.write(data);
        outStream.flush();
        outStream.close();
    }

    @Override
    public boolean isFileExists() {
        return new File(filePath).exists();
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
