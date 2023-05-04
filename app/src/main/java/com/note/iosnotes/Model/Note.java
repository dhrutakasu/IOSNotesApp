package com.note.iosnotes.Model;

import java.util.Arrays;
import java.util.Date;

public class Note {
    private long id;
    private int tagId;
    private String noteTitle;
    private String noteContent;
    private Date dateTimeMills;
    private byte[] imgByteFormat;
    private boolean isDeletedOrNot;
    private boolean isLockedOrNot;
    private boolean isPinnedOrNot;
    private int createWidgetId;
    private int TagFolderId;
    private int intPinOrder;
    private int imgOrientionCode;

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
        this.dateTimeMills = dateModified;
        this.imgByteFormat = imgByteArr;
        this.isDeletedOrNot = isDeleted;
        this.isLockedOrNot = isLocked;
        this.isPinnedOrNot = isPinned;
        this.createWidgetId = widgetId;
        this.TagFolderId = folderId;
        this.intPinOrder = pinOrder;
        this.imgOrientionCode = imgOrientCode;
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

    public void setPinnedOrNot(boolean pinnedOrNot) {
        this.isPinnedOrNot = pinnedOrNot;
    }

    public void setIntPinOrder(int order) {
        this.intPinOrder = order;
    }

    public int getIntPinOrder() {
        return this.intPinOrder;
    }

    public void setNoteTitle(String title) {
        this.noteTitle = title;
    }

    public void setDateTimeMills(Date date) {
        this.dateTimeMills = date;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public long getId() {
        return this.id;
    }

    public boolean isPinnedOrNot() {
        return this.isPinnedOrNot;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public Date getDateTimeMills() {
        return dateTimeMills;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public boolean isLockedOrNot() {
        return isLockedOrNot;
    }

    public void setTagFolderId(int id) {
        this.TagFolderId = id;
    }

    public void setLockedOrNot(boolean lockedOrNot) {
        this.isLockedOrNot = lockedOrNot;
    }

    public void setImgByteFormat(byte[] bytes) {
        this.imgByteFormat = bytes;
    }

    public void setImgOrientionCode(int code) {
        this.imgOrientionCode = code;
    }

    public int getTagFolderId() {
        return this.TagFolderId;
    }

    public byte[] getImgByteFormat() {
        return imgByteFormat;
    }

    public int getImgOrientionCode() {
        return imgOrientionCode;
    }

    public boolean isDeletedOrNot() {
        return isDeletedOrNot;
    }

    public void setDeletedOrNot(boolean deletedOrNot) {
        this.isDeletedOrNot = deletedOrNot;
    }

    public int getCreateWidgetId() {
        return createWidgetId;
    }

    public void setCreateWidgetId(int id) {
        this.createWidgetId = id;
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
                ", dateModified=" + dateTimeMills +
                ", imgByteArr=" + Arrays.toString(imgByteFormat) +
                ", isDeleted=" + isDeletedOrNot +
                ", isLocked=" + isLockedOrNot +
                ", isPinned=" + isPinnedOrNot +
                ", widgetId=" + createWidgetId +
                ", folderId=" + TagFolderId +
                ", pinOrder=" + intPinOrder +
                ", imgOrientCode=" + imgOrientionCode +
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
