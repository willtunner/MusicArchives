package com.greencode.musicarchivebackend.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.service.MongoService;
import com.greencode.musicarchivebackend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Tag(name = "User Controller", description = "Operations related to user management")
public class IndexController {
    private final StorageService storageService;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private MongoService mongoService;

    @Autowired
    public IndexController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("home")
    @Operation(summary = "Get all songs details", description = "Gets details of an all song in the music4all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Song not found"),
            @ApiResponse(responseCode = "403", description = "Expire link in S3")
    })
    public String getHomepage(Model model) {
        int oneHour = 3600000;
        Date expiration = new Date(System.currentTimeMillis() + oneHour);
        List<String> urlSongs = new ArrayList<>();
        List<String> fileNames = storageService.getSongFileNames();
//        List<ObjectIndex> objectIndexList = new ArrayList<>();
//
//        fileNames.forEach(fileName -> {
//          GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("musicarchives", fileName)
//                    .withMethod(HttpMethod.GET)
//                    .withExpiration(expiration);
//            URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
//            System.out.println(presignedUrl);
//            urlSongs.add(presignedUrl.toString());
//        });
//
//        if (fileNames.size() == urlSongs.size()) {
//            for (int i =0; i < fileNames.size(); i++) {
//                ObjectIndex objectIndex = new ObjectIndex();
//                String nameRefactor = fileNames.get(i).substring(fileNames.get(i).indexOf("_") + 1, fileNames.get(i).lastIndexOf("."));
//                objectIndex.setFileNameRefactor(nameRefactor);
//                objectIndex.setFileName(fileNames.get(i));
//                objectIndex.setUrlSong(urlSongs.get(i));
//                objectIndexList.add(objectIndex);
//            }
//        }
        List<Song> songs = mongoService.getSongs();
        model.addAttribute("objectIndexList", songs);
        return "home";
    }

    @GetMapping("login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/home/delete")
    public String deleteFileHome(@RequestParam("delete") String id) {
        if (id != null && !id.isEmpty()) storageService.deleteFile(id);
        return "redirect:/home";
    }

    @PostMapping("/home")
    public String handleFileUpload(@RequestParam("file")MultipartFile file) {
        if (!file.getContentType().startsWith("audio/")) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "O arquivo fornecido não é um tipo de áudio suportado.");
        }
        storageService.uploadFile(file);
        return "redirect:/home";
    }

}



class ObjectIndex {
    private String fileName;

    private String fileNameRefactor;
    private String urlSong;

    public String getFileNameRefactor() {
        return fileNameRefactor;
    }

    public void setFileNameRefactor(String fileNameRefactor) {
        this.fileNameRefactor = fileNameRefactor;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }
}
