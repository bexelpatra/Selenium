package com.test.enums;

public enum ChromePlatform {
    LINUX64("linux64","chromedriver-linux64","chromedriver"),
    MAC_ARM64("mac-arm64","chromedriver-mac-arm64","chromedriver"),
    MAC_X64("mac-x64","chromedriver-mac-x64","chromedriver"),
    WIN32("win32","chromedriver-win32","chromedriver.exe"),
    WIN64("win64","chromedriver-win64","chromedriver.exe"),
    ;
    private String name;
    private String suffix;
    private String regex;
    private String exe;
    
    public String getSuffix() {
        return suffix;
    }
    public String getRegex() {
        return regex;
    }
    public String getName() {
        return name;
    }
    public String getExe() {
        return exe;
    }
    private ChromePlatform(String name, String suffix,String exe) {
        this.name = name;
        this.suffix = suffix;
        this.regex = String.format("\\bhttps://[\\w.-]+(?:\\.[\\w.-]+)+(?:/[^\\s]*)?%s\\.zip\\b", suffix);
        this.exe = exe;

    }
    

}
