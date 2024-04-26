package com.greencode.musicarchivebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@PropertySource("classpath:credentials.properties")
public class CredentialsConfig {

//    @Value("${cloud.aws.credentials.access-key}")
//    private String awsAccessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String awsSecretKey;
//
//    @Value("${spring.data.mongodb.uri}")
//    private String mongoUri;

    @Value("${AWS_ACCESS_KEY}")
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY}")
    private String awsSecretKey;

    @Value("${MONGO_URI}")
    private String mongoUri;



    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public String getMongoUri() {
        return mongoUri;
    }
}
