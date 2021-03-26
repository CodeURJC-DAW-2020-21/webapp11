package es.urjc.code.daw.marketplace.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final String MESSAGE = "Access denied";

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) {

        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(!path.startsWith("/api")) {
            response.sendRedirect(request.getContextPath() + "/error");
        }

        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, MESSAGE);
        outputStream.flush();

    }

}

