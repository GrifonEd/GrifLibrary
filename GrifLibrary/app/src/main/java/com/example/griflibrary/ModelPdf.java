package com.example.griflibrary;

public class ModelPdf {

    String uid,id,title,description,categoryId,url,volume,year,author,category,subscription;
    long timestamp, viewsCount, downloadsCount;

    public ModelPdf() {
    }

    public  ModelPdf(String uid, String id, String title , String description, String categoryId, String url, String volume, String year, String author, long timestamp, long viewsCount, long downloadsCount,String category,String subscription){

        this.subscription = subscription;
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.category = category;
        this.url = url;
        this.volume = volume;
        this.year = year;
        this.author = author;
        this.timestamp = timestamp;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }



    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public void setDownloadsCount(long downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public String getUid() {
        return uid;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getUrl() {
        return url;
    }

    public String getVolume() {
        return volume;
    }


    public String   getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public long getDownloadsCount() {
        return downloadsCount;
    }
}
