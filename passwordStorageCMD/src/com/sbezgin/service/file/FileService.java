package com.sbezgin.service.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    InputStream getCurrentFileInputStream() throws FileNotFoundException;
    void saveDate(byte[] data) throws IOException;
}
