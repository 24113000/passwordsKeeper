package com.sbezgin;

import com.sbezgin.service.file.FileService;
import com.sbezgin.service.file.impl.FileServiceImpl;
import com.sbezgin.service.properties.PropertiesDataHolder;
import com.sbezgin.service.properties.PropertyService;
import com.sbezgin.service.properties.impl.PropertyServiceImpl;
import com.sbezgin.service.security.SecurityProvider;
import com.sbezgin.service.security.impl.SecurityProviderImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Path path = Paths.get("c:/tmp/test.pass");
        byte[] data = Files.readAllBytes(path);

        SecurityProvider securityProvider = new SecurityProviderImpl();
        byte[] decryptData = securityProvider.decryptData(data);
        PropertiesDataHolder dataHolder = propertyService.readProperties(new ByteArrayInputStream(decryptData));

/*
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decryptData), "UTF-8"));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = br.readLine()) != null){
            if (!line.contains("rustorka")) {
                stringBuilder.append(line).append("\n");
            }
        }
*/

//        PropertiesDataHolder dataHolder = propertyService.readProperties(new FileInputStream("c:/tmp/encrypted.passwords.raw"));
        String convert2text = propertyService.convert2text(dataHolder);
        System.out.println(convert2text);


//        SecurityProvider securityProvider = new SecurityProviderImpl();
        /*byte[] bytes = securityProvider.encryptData(stringBuilder.toString().getBytes("UTF-8"));
        FileService fileService = new FileServiceImpl();
        fileService.saveDate(bytes);*/


    }

    public static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            byte c = 0;
            while ((c = (byte) r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

}
