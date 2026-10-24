package it.alessandrozap.utilsapi.adapters;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ServerVersion(int major, int minor, int patch) implements Comparable<ServerVersion> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?");
    private static ServerVersion current;

    public static ServerVersion current() {
        if (current == null) current = parse(Bukkit.getBukkitVersion());
        return current;
    }

    public static ServerVersion parse(String version) {
        if (version == null) throw new IllegalArgumentException("Version cannot be null!");
        Matcher m = VERSION_PATTERN.matcher(version.trim());
        if (!m.find()) throw new IllegalArgumentException("Invalid version: " + version);
        return new ServerVersion(
                Integer.parseInt(m.group(1)),
                m.group(2) != null ? Integer.parseInt(m.group(2)) : 0,
                m.group(3) != null ? Integer.parseInt(m.group(3)) : 0
        );
    }

    public boolean isAtLeast(int major, int minor) {
        return isAtLeast(major, minor, 0);
    }

    public boolean isAtLeast(int major, int minor, int patch) {
        return compareTo(new ServerVersion(major, minor, patch)) >= 0;
    }

    @Override
    public int compareTo(ServerVersion o) {
        if (major != o.major) return Integer.compare(major, o.major);
        if (minor != o.minor) return Integer.compare(minor, o.minor);
        return Integer.compare(patch, o.patch);
    }

    @Override
    public String toString() {
        return patch == 0 ? major + "." + minor : major + "." + minor + "." + patch;
    }
}
