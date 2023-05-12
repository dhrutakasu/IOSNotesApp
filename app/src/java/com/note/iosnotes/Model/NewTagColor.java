package com.note.iosnotes.Model;

public class NewTagColor {
    private int tagColorIcon;
    private int tagColorId;
    private String tagColorName;

    public NewTagColor(int tagColorIcon, String tagColorName, int tagColorId) {
        this.tagColorIcon = tagColorIcon;
        this.tagColorName = tagColorName;
        this.tagColorId = tagColorId;
    }

    public int getTagColorIcon() {
        return this.tagColorIcon;
    }

    public String getTagColorName() {
        return this.tagColorName;
    }

    public void setTagColorIcon(int i) {
        this.tagColorIcon = i;
    }

    public void setTagColorName(String str) {
        this.tagColorName = str;
    }

    public int getTagColorId() {
        return this.tagColorId;
    }
}
