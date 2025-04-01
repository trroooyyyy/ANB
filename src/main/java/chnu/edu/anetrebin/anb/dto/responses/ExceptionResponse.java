package chnu.edu.anetrebin.anb.dto.responses;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime time,
        String message
) {}
