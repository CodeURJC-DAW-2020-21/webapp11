package es.urjc.code.daw.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    /**
     * Stores the given picture for the specified user.
     *
     * @param userId the given user identifier
     * @param profilePicture the picture to be uploaded for the user
     * @return the filename of the stored picture
     */
    String savePicture(Long userId, MultipartFile profilePicture);

}
