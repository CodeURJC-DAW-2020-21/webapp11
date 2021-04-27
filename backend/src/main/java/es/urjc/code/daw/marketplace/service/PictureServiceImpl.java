package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * A simple implementation for the {@link PictureService}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    @Override
    public String savePicture(Long userId, MultipartFile profilePicture) {
        Objects.requireNonNull(profilePicture.getOriginalFilename());

        final String BASE_DIR = "user-profile-pictures";

        String fileName = StringUtils.cleanPath(profilePicture.getOriginalFilename());
        String extension = FilenameUtils.getExtension(fileName);
        String newName = userId + "." + extension;


        String filename = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));
        Path uploadPath = Paths.get(BASE_DIR + File.separator + newName);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        try (InputStream inputStream = profilePicture.getInputStream()) {
            Path filePath = new File(BASE_DIR + File.separator + newName).toPath();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not save image file: " + filename, ioe);
        }

        return newName;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public String getEncodedPicture(User user) {
        if(user.getProfilePictureFilename() != null) {
            // Find that user's picture
            final String PICTURES_FOLDER = "user-profile-pictures/";
            File file = new File(PICTURES_FOLDER + user.getProfilePictureFilename());
            if(file.exists()) {
                String mimeType = Files.probeContentType(file.toPath());
                InputStream targetStream = new FileInputStream(file);
                StringBuffer builder = new StringBuffer();
                builder.append("data:");
                builder.append(mimeType);
                builder.append(";base64,");
                builder.append(Base64Utils.encodeToString(IOUtils.toByteArray(targetStream)));
                return builder.toString();
            }
        }
        return "";
    }

}
