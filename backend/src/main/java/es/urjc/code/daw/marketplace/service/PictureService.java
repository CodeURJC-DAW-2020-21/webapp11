package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
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

    /**
     * Returns the encoded image in base64 or an empty string if the
     * user has no associated image.
     *
     * @param userId the given user identifier
     * @return the base64 encoded image or an empty string
     */
    String getEncodedPicture(User user);

}
