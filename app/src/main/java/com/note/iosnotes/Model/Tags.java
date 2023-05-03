package com.note.iosnotes.Model;

public class Tags {
    String TagName;
    int Id, ColorCodeId, CounterNote;

    public Tags(int id, String tagName, int colorCodeId, int counterNote) {
        Id = id;
        TagName = tagName;
        ColorCodeId = colorCodeId;
        CounterNote = counterNote;
    }

    public Tags(int id, String tagName) {
        Id = id;
        TagName = tagName;
    }

    public Tags(String tagName, int colorCodeId, int counterNote) {
        TagName = tagName;
        ColorCodeId = colorCodeId;
        CounterNote = counterNote;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public int getColorCodeId() {
        return ColorCodeId;
    }

    public void setColorCodeId(int colorCodeId) {
        ColorCodeId = colorCodeId;
    }

    public int getCounterNote() {
        return CounterNote;
    }

    public void setCounterNote(int counterNote) {
        CounterNote = counterNote;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "TagName='" + TagName + '\'' +
                ", Id=" + Id +
                ", ColorCodeId=" + ColorCodeId +
                ", CounterNote=" + CounterNote +
                '}';
    }
}
