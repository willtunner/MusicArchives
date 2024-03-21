package com.greencode.musicarchivebackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Song {

    @Id
    private String id;

    private String fileName;
    private String title;
    private String artist;
    private String duration;
    private String urlSong;
    private boolean isFavorited = false;

}
