package it.alessandrozap.utilsapi.interfaces;

import it.alessandrozap.utilsapi.managers.file.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public interface IConfigEntry {

    String getPath();
    Object getDefaultValue();
    Object getCachedValue();
    void setCachedValue(Object value);

    default void reload(YamlConfiguration config) {
        setCachedValue(config.get(getPath(), getDefaultValue()));
    }

    default String getString() {
        Object value = getCachedValue();
        return value instanceof String ? (String) value : "";
    }

    default boolean getBoolean() {
        Object value = getCachedValue();
        return value instanceof Boolean ? (Boolean) value : true;
    }

    default int getInt() {
        Object value = getCachedValue();
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    default long getLong() {
        Object value = getCachedValue();
        return value instanceof Number ? ((Number) value).longValue() : 0L;
    }

    @SuppressWarnings("unchecked")
    default List<String> getStringList() {
        Object value = getCachedValue();
        return value instanceof List<?> ? (List<String>) value : List.of();
    }

    default void set(FileManager configFile, Object object) {
        setCachedValue(object);
        configFile.getConfig().set(getPath(), object);
        configFile.saveConfig();
    }
}
