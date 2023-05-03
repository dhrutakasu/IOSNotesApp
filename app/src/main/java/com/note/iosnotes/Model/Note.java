package com.note.iosnotes.Model;

import java.util.Arrays;
import java.util.Date;

public class Note {
    private long id;
    private int tagId;
    private String noteTitle;
    private String noteContent;
    private Date dateModified;
    private byte[] imgByteArr;
    private boolean isDeleted;
    private boolean isLocked;
    private boolean isPinned;
    private int widgetId;
    private int folderId;
    private int pinOrder;
    private int imgOrientCode;

    private boolean titleBold;
    private boolean titleItalic;
    private boolean titleUnderline;
    private boolean titleStrike;
    private boolean contentBold;
    private boolean contentItalic;
    private boolean contentUnderline;
    private boolean contentStrike;
    private int align;

    public Note(long id, int tagId, String noteTitle, String noteContent, Date dateModified, byte[] imgByteArr, boolean isDeleted, boolean isLocked, boolean isPinned, int widgetId, int folderId, int pinOrder, int imgOrientCode,boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align) {
        this.id = id;
        this.tagId = tagId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.dateModified = dateModified;
        this.imgByteArr = imgByteArr;
        this.isDeleted = isDeleted;
        this.isLocked = isLocked;
        this.isPinned = isPinned;
        this.widgetId = widgetId;
        this.folderId = folderId;
        this.pinOrder = pinOrder;
        this.imgOrientCode = imgOrientCode;
        this.titleBold = titleBold;
        this.titleItalic = titleItalic;
        this.titleUnderline = titleUnderline;
        this.titleStrike = titleStrike;
        this.contentBold = contentBold;
        this.contentItalic = contentItalic;
        this.contentUnderline = contentUnderline;
        this.contentStrike = contentStrike;
        this.align = align;
    }

    public Note() {

    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPinned(boolean pinned) {
        this.isPinned = pinned;
    }

    public void setPinOrder(int order) {
        this.pinOrder = order;
    }

    public int getPinOrder() {
        return this.pinOrder;
    }

    public void setNoteTitle(String title) {
        this.noteTitle = title;
    }

    public void setDateModified(Date date) {
        this.dateModified = date;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public long getId() {
        return this.id;
    }

    public boolean isPinned() {
        return this.isPinned;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setFolderId(int id) {
        this.folderId = id;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public void setImgByteArr(byte[] bytes) {
        this.imgByteArr = bytes;
    }

    public void setImgOrientCode(int code) {
        this.imgOrientCode = code;
    }

    public int getFolderId() {
        return this.folderId;
    }

    public byte[] getImgByteArr() {
        return imgByteArr;
    }

    public int getImgOrientCode() {
        return imgOrientCode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int id) {
        this.widgetId = id;
    }

    public boolean isTitleBold() {
        return titleBold;
    }

    public void setTitleBold(boolean titleBold) {
        this.titleBold = titleBold;
    }

    public boolean isTitleItalic() {
        return titleItalic;
    }

    public void setTitleItalic(boolean titleItalic) {
        this.titleItalic = titleItalic;
    }

    public boolean isTitleUnderline() {
        return titleUnderline;
    }

    public void setTitleUnderline(boolean titleUnderline) {
        this.titleUnderline = titleUnderline;
    }

    public boolean isTitleStrike() {
        return titleStrike;
    }

    public void setTitleStrike(boolean titleStrike) {
        this.titleStrike = titleStrike;
    }

    public boolean isContentBold() {
        return contentBold;
    }

    public void setContentBold(boolean contentBold) {
        this.contentBold = contentBold;
    }

    public boolean isContentItalic() {
        return contentItalic;
    }

    public void setContentItalic(boolean contentItalic) {
        this.contentItalic = contentItalic;
    }

    public boolean isContentUnderline() {
        return contentUnderline;
    }

    public void setContentUnderline(boolean contentUnderline) {
        this.contentUnderline = contentUnderline;
    }

    public boolean isContentStrike() {
        return contentStrike;
    }

    public void setContentStrike(boolean contentStrike) {
        this.contentStrike = contentStrike;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", tagId=" + tagId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", dateModified=" + dateModified +
                ", imgByteArr=" + Arrays.toString(imgByteArr) +
                ", isDeleted=" + isDeleted +
                ", isLocked=" + isLocked +
                ", isPinned=" + isPinned +
                ", widgetId=" + widgetId +
                ", folderId=" + folderId +
                ", pinOrder=" + pinOrder +
                ", imgOrientCode=" + imgOrientCode +
                ", titleBold=" + titleBold +
                ", titleItalic=" + titleItalic +
                ", titleUnderline=" + titleUnderline +
                ", titleStrike=" + titleStrike +
                ", contentBold=" + contentBold +
                ", contentItalic=" + contentItalic +
                ", contentUnderline=" + contentUnderline +
                ", contentStrike=" + contentStrike +
                ", align=" + align +
                '}';
    }
}
