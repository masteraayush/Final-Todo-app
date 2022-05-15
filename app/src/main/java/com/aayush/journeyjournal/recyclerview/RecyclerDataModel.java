package com.aayush.journeyjournal.recyclerview;

public class RecyclerDataModel {
    String journalID;
    String ImgResource;
    String JournalTitle;
    String JournalLocation;
    String JournalDate;
    String JournalThought;
    

    public RecyclerDataModel(String nJournalID, String nImgResource, String nJournalTitle, String nJournalLocation, String nJournalDate, String nJournalThought){
        this.journalID = nJournalID;
        this.ImgResource = nImgResource;
        this.JournalTitle = nJournalTitle;
        this.JournalLocation = nJournalLocation;
        this.JournalDate = nJournalDate;
        this.JournalThought = nJournalThought;
    }

    public String getJournalID() {
        return journalID;
    }

    public String getImgResource() {
        return ImgResource;
    }

    public String getJournalTitle() {
        return  JournalTitle;
    }

    public String getJournalLocation() {
        return JournalLocation;
    }

    public String getJournalDate() {
        return JournalDate;
    }

    public String getJournalThought() {
        return  JournalThought;
    }
}
