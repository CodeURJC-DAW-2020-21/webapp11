package es.urjc.code.daw.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    String savePicture(Long userId, MultipartFile profilePicture);

}
