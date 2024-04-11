package com.greencode.musicarchivebackend.controller;

import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*")
@Tag(name = "Storage Controller", description = "Operations related to storage file system and file upload/download/delete file operations")
public class StorageController {
    @Autowired
    private StorageService service;
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new Song in S3", description = "Create a new song in s3")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Song created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Song> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "Get songs storage by name", description = "Gets song by name in the music4all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    @Operation(summary = "Delete an song by name", description = "Delete an song from the music4all by fileName.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Song deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
