package io.loong95.light.chat;

/**
 * @author linyunlong
 */
public class ThemeItem {
    private String themeName;
    private String className;

    public ThemeItem(String themeName, String className) {
        this.themeName = themeName;
        this.className = className;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return themeName;
    }
}
