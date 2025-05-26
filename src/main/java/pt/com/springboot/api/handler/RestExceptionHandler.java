package pt.com.springboot.api.handler;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.error.ResourceNotFoundException;
import pt.com.springboot.api.error.details.InternalServerErrorDetails;
import pt.com.springboot.api.error.details.ResourceNotFoundDetails;
import pt.com.springboot.api.error.details.ValidationErrorDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Exception Handler
 *
 * @version 1.0
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    LocalDateTime localDateTime = LocalDateTime.now();
    String localDateTimeString = localDateTime.toString();

    /**
     * Handle resource not found exception
     *
     * @param rfnException
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException) {
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(localDateTimeString)
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail(rfnException.getMessage())
                .transactionId(MDC.get("transactionId"))
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle method argument not valid exception
     *
     * @param manvException
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(localDateTimeString)
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field Validation Error")
                .detail("Field Validation Error")
                .transactionId(MDC.get("transactionId"))
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle internal server error exception
     *
     * @param intException
     * @return ResponseEntity<?>
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerError(InternalServerErrorException intException) {
        InternalServerErrorDetails rnfDetails = InternalServerErrorDetails.Builder
                .newBuilder()
                .timestamp(localDateTimeString)
                .status(HttpStatus.NOT_FOUND.value())
                .title("Internal Server Error")
                .detail(intException.getMessage())
                .transactionId(MDC.get("transactionId"))
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }
}
