package com.greencode.musicarchivebackend.repository;

import com.greencode.musicarchivebackend.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface SongRepository extends MongoRepository<Song, String  > {

    boolean existsByTitleEquals(String title);
    boolean existsByFileNameEquals(String fileName);
}
