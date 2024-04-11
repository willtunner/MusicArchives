package com.greencode.musicarchivebackend.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.greencode.musicarchivebackend.model.Song;
import com.greencode.musicarchivebackend.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private MongoService mongoService;
    @Autowired
    private SongRepository songRepository;

    public Song uploadFile(MultipartFile file) {
        int oneHour = 3600000;
        Date expiration = new Date(System.currentTimeMillis() + oneHour);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File fileObject = convertMultiPartFileToFile(file);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("musicarchives", fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        try {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObject));

            return mongoService.saveSongMongo(presignedUrl.toString(), file, fileName);
        } finally {
            boolean deleted = fileObject.delete();
            if (!deleted) {
                System.err.println("Falha ao excluir o arquivo local: " + fileObject.getAbsolutePath());
            }
        }
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String songId) {
        String fileName = songRepository.findById(songId).get().getFileName();
        songRepository.deleteById(songId);
        s3Client.deleteObject(bucketName, fileName);
        System.out.println(fileName + " removed ...");
        return fileName + " removed ...";
    }

    public File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public List<String> getSongFileNames() {
        ListObjectsV2Result result = s3Client.listObjectsV2("musicarchives");
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        return objects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    public String getFileUrl(String fileName, String bucketName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

}
