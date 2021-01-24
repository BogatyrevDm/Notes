package com.example.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

class Note implements Parcelable {
    private String name; //Имя заметки
    private String description; //Описание
    private Date creationDate; //Дата создания
    private boolean isImportant; //Признак важности
    private String content; //Содержимое заметки

    public Note(String name, String description, Date creationDate, boolean isImportant, String content) {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.isImportant = isImportant;
        this.content = content;
    }
    public Note(String name) {
        this.name = name;
    }

    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
        isImportant = in.readByte() != 0;
        content = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public String getStringCreationDate() {
        return String.valueOf(getCreationDate());
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (isImportant ? 1 : 0));
        dest.writeString(content);
    }
}
