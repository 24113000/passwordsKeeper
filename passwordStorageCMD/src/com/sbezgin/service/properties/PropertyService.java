package com.sbezgin.service.properties;

import java.io.IOException;
import java.io.InputStream;

public interface PropertyService {
    PropertiesDataHolder readProperties(InputStream in) throws IOException;
    String convert2text(PropertiesDataHolder dataHolder);
}
