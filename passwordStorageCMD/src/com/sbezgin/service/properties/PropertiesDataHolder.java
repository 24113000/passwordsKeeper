package com.sbezgin.service.properties;

import java.util.*;

public class PropertiesDataHolder {
    public static final String SECRET_PROPERTIES = "secret";
    private Set<String> groups = new HashSet<>();
    private Map<String, Map<String, Set<PropertyDTO>>> map = new HashMap<>();
    private Map<String, Set<PropertyDTO>> secretMap = new HashMap<>();

    public void addGroups(Set<String> groups) {
        for (String group : groups) {
            if (!group.equals(SECRET_PROPERTIES)) {
                this.groups.add(group);
            }
        }
    }

    public void addProperty(String groupName, String name, String key, String value) {
        if (groupName.equals(SECRET_PROPERTIES)) {
            addSecretProperties(name, key, value);
        } else {
            addRegularProperties(groupName, name, key, value);
        }
    }

    private void addSecretProperties(String name, String key, String value) {
        Set<PropertyDTO> dtos = secretMap.get(name);
        if (dtos == null) {
            dtos = new HashSet<>();
            secretMap.put(name, dtos);
        }

        PropertyDTO prop = new PropertyDTO();
        prop.setKey(key);
        prop.setValue(value);
        prop.setName(name);

        dtos.add(prop);
    }

    private void addRegularProperties(String groupName, String name, String key, String value) {
        Map<String, Set<PropertyDTO>> propertyDTOMap = map.get(groupName);
        if (propertyDTOMap == null) {
            propertyDTOMap = new HashMap<>();
            map.put(groupName, propertyDTOMap);
        }

        Set<PropertyDTO> dtos = propertyDTOMap.get(name);
        if (dtos == null) {
            dtos = new HashSet<>();
            propertyDTOMap.put(name, dtos);
        }

        PropertyDTO prop = new PropertyDTO();
        prop.setKey(key);
        prop.setValue(value);
        prop.setName(name);

        dtos.add(prop);
    }

    public Set<PropertyDTO> getProperties(String groupName, String name) {
        Map<String, Set<PropertyDTO>> dtoMap = map.get(groupName);
        return dtoMap.get(name);
    }

    public Set<String> getNames(String groupName) {
        Map<String, Set<PropertyDTO>> dtoMap = map.get(groupName);
        return dtoMap.keySet();
    }

    public Set<String> getGroups() {
        return groups;
    }

    public Set<PropertyDTO> getSecretProperties(String name) {
        return secretMap.get(name);
    }

    public Set<String> getSecretNames() {
        return secretMap.keySet();
    }
}
