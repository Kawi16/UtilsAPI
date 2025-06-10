package it.alessandrozap.config;

import it.alessandrozap.interfaces.IConfigEnum;

public class ConfigEntry {
    public static IConfigEnum of(String path, Object defaultValue) {
        return new IConfigEnum() {
            private Object cached = defaultValue;
            @Override public String getPath() { return path; }
            @Override public Object getDefaultValue() { return defaultValue; }
            @Override public Object getCachedValue() { return cached; }
            @Override public void setCachedValue(Object value) { this.cached = value; }
        };
    }
}
