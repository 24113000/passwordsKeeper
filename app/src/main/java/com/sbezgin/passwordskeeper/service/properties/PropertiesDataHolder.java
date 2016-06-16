package com.sbezgin.passwordskeeper.service.properties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertiesDataHolder {
    public static final String SECRET_PROPERTIES = "secret";
    private Set<String> groups = new HashSet<>();
    private Set<String> newGroups = new HashSet<>();
    private Map<String, Map<String, Set<PropertyDTO>>> map = new HashMap<>();
    private Map<String, Map<String, Set<PropertyDTO>>> newMap = new HashMap<>();

    public void addGroups(Set<String> groups) {
        for (String group : groups) {
            this.groups.add(group);
        }
    }

    public void addProperty(String groupName, String name, String key, String value) {
        addRegularProperties(groupName, name, key, value, map);
    }

    public void addNewProperty(String groupName, String name, String key, String value) {
        addRegularProperties(groupName, name, key, value, newMap);
        newGroups.add(groupName);
    }

    public boolean isChanged() {
        return newMap.size() > 0;
    }

    private void addRegularProperties(String groupName, String name, String key, String value, Map<String, Map<String, Set<PropertyDTO>>> currMap) {
        Map<String, Set<PropertyDTO>> propertyDTOMap = this.map.get(groupName);
        if (propertyDTOMap == null) {
            propertyDTOMap = new HashMap<>();
            currMap.put(groupName, propertyDTOMap);
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
        Set<PropertyDTO> result = new HashSet<>();
        if (dtoMap != null) {
            result.addAll(dtoMap.get(name));
        }
        Map<String, Set<PropertyDTO>> newDtoMap = newMap.get(groupName);
        if (newDtoMap != null) {
            result.addAll(newDtoMap.get(name));
        }
        return result;
    }

    public Set<String> getNames(String groupName) {
        Map<String, Set<PropertyDTO>> dtoMap = map.get(groupName);
        Set<String> result = new HashSet<>();
        if (dtoMap != null) {
            result.addAll(dtoMap.keySet());
        }
        Map<String, Set<PropertyDTO>> newDTOMap = newMap.get(groupName);
        if (newDTOMap != null) {
            result.addAll(newDTOMap.keySet());
        }
        return result;
    }

    public Set<String> getGroups() {
        Set<String> result = new HashSet<>(groups);
        result.addAll(newGroups);
        return result;
    }

    public void removeGroup(String group) {
        removeProperty(group, null, null);
    }

    public void removeName(String group, String name) {
        removeProperty(group, name, null);
    }

    public void removeProperty(String group, String name, PropertyDTO item) {
        Map<String, Set<PropertyDTO>> namesMap = map.get(group);
        if (namesMap != null) {
            removeProperty(group, name, item, map);
        } else {
            namesMap = newMap.get(group);
            if (namesMap==null) {
                throw new RuntimeException("Cannot get properties for group: " + group);
            }
            removeProperty(group, name, item, newMap);
        }
    }

    private void removeProperty(String group, String name, PropertyDTO item, Map<String, Map<String, Set<PropertyDTO>>> map) {
        if (name == null) {
            map.remove(group);
            groups.remove(group);
            return;
        }

        if (item == null) {
            Map<String, Set<PropertyDTO>> dtoMap = map.get(group);
            dtoMap.remove(name);
            return;
        }

        Map<String, Set<PropertyDTO>> dtoMap = map.get(group);
        dtoMap.remove(item.getKey());
    }
}
