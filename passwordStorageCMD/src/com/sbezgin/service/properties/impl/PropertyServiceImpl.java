package com.sbezgin.service.properties.impl;

import com.sbezgin.service.properties.PropertiesDataHolder;
import com.sbezgin.service.properties.PropertyDTO;
import com.sbezgin.service.properties.PropertyService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertyServiceImpl implements PropertyService{
    @Override
    public PropertiesDataHolder readProperties(InputStream in) throws IOException {
        PropertiesDataHolder propertiesDataHolder = new PropertiesDataHolder();

        Properties props = new Properties();
        props.load(in);

        Set<String> groups = new HashSet<>();
        Set<Map.Entry<String, String>> entries = (Set) props.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            String propKey = entry.getKey();
            String propValue = entry.getValue();

            String[] split = propKey.split("\\.");

            propertiesDataHolder.addProperty(split[0], split[1], split[2], propValue);
            groups.add(split[0]);
        }

        propertiesDataHolder.addGroups(groups);

        return propertiesDataHolder;
    }

    @Override
    public String convert2text(PropertiesDataHolder dataHolder) {
        StringBuilder text = new StringBuilder();
        Set<String> groups = dataHolder.getGroups();
        for (String group : groups) {
            Set<String> names = dataHolder.getNames(group);
            for (String name : names) {
                Set<PropertyDTO> properties = dataHolder.getProperties(group, name);
                for (PropertyDTO property : properties) {
                    addProp(text, group, name, property);
                }
            }
        }

        Set<String> secretNames = dataHolder.getSecretNames();
        for (String secretName : secretNames) {
            Set<PropertyDTO> properties = dataHolder.getSecretProperties(secretName);
            for (PropertyDTO property : properties) {
                addProp(text, PropertiesDataHolder.SECRET_PROPERTIES, secretName, property);
            }
        }

        return text.toString();
    }

    private void addProp(StringBuilder text, String group, String name, PropertyDTO property) {
        text.append(group)
                .append(".")
                .append(name)
                .append(".")
                .append(property.getKey())
                .append("=")
                .append(property.getValue());
        text.append("\n");
    }
}
