package com.greencode.musicarchivebackend.controller;

import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.repository.SongRepository;
import com.greencode.musicarchivebackend.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final StorageService storageService;
    private final SongRepository songRepository;

    public SongController(StorageService storageService, SongRepository songRepository) {
        this.storageService = storageService;
        this.songRepository = songRepository;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getSongs() {
        return ResponseEntity.ok(songRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Song> getSong(@PathVariable String id) {
        Optional<Song> song = songRepository.findById(id);

        if (song.isPresent()) {
            return  ResponseEntity.ok(song.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createSong(@RequestParam(value = "file") MultipartFile file, Song song) {
        if (songRepository.existsByFileNameEquals(song.getFileName()) || songRepository.existsByTitleEquals(song.getTitle())) {
            return ResponseEntity.badRequest().body("taken");
        } else {
            System.out.println("Uploading the file...");
            storageService.uploadFile(file);
            song.setFileName(file.getOriginalFilename());
            Song insertedSong = songRepository.insert(song);
            return new ResponseEntity<>(insertedSong, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song songData) {
        Optional<Song> songOptional = songRepository.findById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            if (songData.getTitle() != null) song.setTitle(songData.getTitle());
            if (songData.getArtist() != null) song.setArtist(songData.getArtist());
            song.setFavorited(songData.isFavorited());
            songRepository.save(song);
            return ResponseEntity.ok(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> deleteSong(@PathVariable String id) {

        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.noContent().build();
        }
    }
}
