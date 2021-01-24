package com.example.notes;

import java.util.Date;

class Note {
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
}
