package com.sbezgin.passwordskeeper.service.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    byte[] getCurrentFileBytes() throws IOException;
    InputStream getCurrentFileInputStream() throws FileNotFoundException;
    void saveDate(byte[] data) throws IOException;
    boolean isFileExists();
}
