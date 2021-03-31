package es.urjc.code.daw.marketplace.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class DecodedBase64MultipartFile implements MultipartFile {

    private final String filename;
    private final String contentType;
    private final byte[] bytes;

    public DecodedBase64MultipartFile(String filename, String contentType, byte[] bytes) {
        this.filename = filename;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File destination) throws IOException, IllegalStateException {
        new FileOutputStream(destination).write(bytes);
    }

}