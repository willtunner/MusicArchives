package com.greencode.musicarchivebackend.service;

import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@Slf4j
public class MongoService {
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private  StorageService storageService;

    public void saveSongMongo(String urlSong, MultipartFile file) {
        int durationSeconds = 0;
        File fileObject = storageService.convertMultiPartFileToFile(file);

        Song song = new Song();
        song.setUrlSong(urlSong);

        try {
            AudioFile audioFile = AudioFileIO.read(fileObject);
            Tag tag = audioFile.getTag();
            String artist = tag.getFirst(FieldKey.ARTIST);
            song.setArtist(artist);

            durationSeconds = audioFile.getAudioHeader().getTrackLength();
            String duration = secondsToMinutes(durationSeconds);
            song.setDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();

        }
        song.setTitle(file.getOriginalFilename());

        songRepository.save(song);

    }

    private String secondsToMinutes(int durationSeconds) {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
