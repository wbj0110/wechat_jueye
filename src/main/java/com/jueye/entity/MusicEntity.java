package com.jueye.entity;

/**
 * Created by soledede on 2015/10/20.
 */
public class MusicEntity {
    private String name;
    private Double popularity;
    private String url;
    private String singer;

    public MusicEntity() {
    }

    public MusicEntity(String name, Double popularity, String url, String singer) {
        this.name = name;
        this.popularity = popularity;
        this.url = url;
        this.singer = singer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
