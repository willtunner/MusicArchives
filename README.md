# Music Archive

> A simple API where you can upload music, storing it on Amazon S3 and saving information in MongoDB.

## Imagens do projeto
<div align="center">
  <img src="/src/images/img.png" width="30%">
  <img src="/src/images/img_1.png" width="30%">
  <img src="/src/images/img_2.png" width="30%">
</div>

```bash
- Start project
- http://localhost:8081/home
```

[//]: # (> **Note**: The script will build the project before running it, and it'll start the containers for the database using `docker compose up -d`.Note**: The script will build the project before running it, and it'll start the containers for the database using `docker compose up -d`.)

### Environment

The API uses some environment variables to set up the database connection, Amazon S3 configuration and the port to run the server. The default values are:

```ini
server.port=8081
spring.data.mongodb.uri=

## Config S3 Bucket
cloud.aws.credentials.access-key=
cloud.aws.credentials.secret-key=
cloud.aws.region.static=
cloud.aws.stack.auto=false

application.bucket.name=musicarchives

spring.servlet.multipart.enabled=true
spring.servlet.multipart.file-size-threshold=12MB
spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=30MB
```

The API has 5 endpoints:

- `GET /api/songs`: Returns all the sounds in the database
- `GET /api/songs/:id`: Returns a sound by its id
- `PUT /api/songs/:id`: Update an song
- `DELETE /api/songs/:id`: Delete an song
- `POST /api/songs`: Creates a new song

### Adding a song

To add a song, simply click on the `Enviar` input and choose your audio file. The song will be sent via a `POST` request to `/api/songs/` and returned in JSON format.

```json
{
  "id": "661738b173b273245a531536",
  "fileName": "1712797872212_RockIntro2.mp3",
  "title": "RockIntro2.mp3",
  "artist": "AUDIONAUTIX.COM",
  "duration": "00:14",
  "urlSong": "https://musicarchives.s3.sa-east-1.amazonaws.com/1712797872212_RockIntro2.mp3",
  "favorited": false
}
```

### Technologies used:
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white) ![JavaScript](https://img.shields.io/badge/-JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white)
![MongoDB](https://img.shields.io/badge/-MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white) <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white">

