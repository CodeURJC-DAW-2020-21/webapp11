package es.urjc.code.daw.marketplace.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

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

}
