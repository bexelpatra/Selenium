package com.test.dto;

public class MergeInfo {
    private String fileName;
    private int contentHeight;
    private int contentWidth;
    
    private int lastScrollHeigh;
    private int lastScrollWidth;

    public String getFileName() {
        return fileName;
    }
    public int getContentHeight() {
        return contentHeight;
    }
    public int getLastScrollHeigh() {
        return lastScrollHeigh;
    }
    public MergeInfo(String fileName, int contentHeight, int contentWidth, int lastScrollHeigh, int lastScrollWidth) {
        this.fileName = fileName;
        this.contentHeight = contentHeight;
        this.contentWidth = contentWidth;
        this.lastScrollHeigh = lastScrollHeigh;
        this.lastScrollWidth = lastScrollWidth;
    }
    public int getContentWidth() {
        return contentWidth;
    }
    public int getLastScrollWidth() {
        return lastScrollWidth;
    }
    

}
