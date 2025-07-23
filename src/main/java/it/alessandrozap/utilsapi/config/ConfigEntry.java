package it.alessandrozap.utilsapi.config;

import it.alessandrozap.utilsapi.interfaces.IConfigEntry;
import it.alessandrozap.utilsapi.managers.file.FileManager;

public class ConfigEntry {
    public static IConfigEntry of(String path, Object defaultValue) {
        return new IConfigEntry() {
            private Object cached = defaultValue;
            @Override public String getPath() { return path; }
            @Override public Object getDefaultValue() { return defaultValue; }
            @Override public Object getCachedValue() { return cached; }
            @Override public void setCachedValue(Object value) { this.cached = value; }
        };
    }

    public static IConfigEntry of(FileManager file, String path, Object defaultValue) {
        IConfigEntry entry = of(path, defaultValue);
        entry.reload(file.getConfig());
        return entry;
    }
}
