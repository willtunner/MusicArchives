package com.greencode.musicarchivebackend.controller;

import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.repository.SongRepository;
import com.greencode.musicarchivebackend.service.SongService;
import com.greencode.musicarchivebackend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
@Tag(name = "Song Controller", description = "Operations related to song management")
public class SongController {
    private final StorageService storageService;
    private final SongRepository songRepository;
    private final SongService songService;

    public SongController(StorageService storageService, SongRepository songRepository, SongService songService) {
        this.storageService = storageService;
        this.songRepository = songRepository;
        this.songService = songService;
    }

    @GetMapping
    @Operation(summary = "Get all songs details", description = "Gets details of an all song in the music4all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<List<Song>> getSongs() {
        return ResponseEntity.ok(songService.getSongs());
    }

    @GetMapping("{id}")
    @Operation(summary = "Get song by id", description = "Gets details of an song by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Song> getSong(@PathVariable String id) {
        Optional<Song> song = songService.getSongById(id);

        return song.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new Song", description = "Create a new song in music4all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Song created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> createSong(@RequestParam(value = "file") MultipartFile file) {
        if (songService.isExist(file.getName(), file.getOriginalFilename())) {
            return ResponseEntity.badRequest().body("There is already a file with that name.");
        } else {
            System.out.println("Uploading the file...");
            Song song = storageService.uploadFile(file);
            return new ResponseEntity<>(song, HttpStatus.CREATED);
        }
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update an song", description = "Updates an existing song in the music4all by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song updated successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song songData) {
        Optional<Song> songOptional = songService.getSongById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            if (songData.getFileName() != null) song.setFileName(songData.getFileName());
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
    @Operation(summary = "Delete an song", description = "Delete an song from the music4all by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Song deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Song> deleteSong(@PathVariable String id) {

        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.noContent().build();
        }
    }
}
