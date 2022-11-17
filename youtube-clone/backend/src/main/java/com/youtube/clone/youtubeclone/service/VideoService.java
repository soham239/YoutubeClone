package com.youtube.clone.youtubeclone.service;

import com.youtube.clone.youtubeclone.model.Video;
import com.youtube.clone.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public void uploadVideo(MultipartFile multipartFile) {
        // Upload the file to AWS S3
        String videoUrl = s3Service.uploadFile(multipartFile);
        Video video = new Video();
        video.setVideoUrl(videoUrl);

        // Save Video to Database
        videoRepository.save(video);
    }
}
