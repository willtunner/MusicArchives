package com.greencode.musicarchivebackend.service;

import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SongService {
    private final StorageService storageService;
    private final SongRepository songRepository;

    public SongService(StorageService storageService, SongRepository songRepository) {
        this.storageService = storageService;
        this.songRepository = songRepository;
    }

    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }

    public Song updateSong(Song songData, String id) {
        Optional<Song> songOptional = getSongById(id);


        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            if (songData.getTitle() != null) song.setTitle(songData.getTitle());
            if (songData.getArtist() != null) song.setArtist(songData.getArtist());
            song.setFavorited(songData.isFavorited());
            return songRepository.save(song);
        }
        return null;

    }


    public Boolean isExist(String fileName, String originalFileName) {
        return songRepository.existsByFileNameEquals(fileName) || songRepository.existsByTitleEquals(originalFileName);
    }
}
