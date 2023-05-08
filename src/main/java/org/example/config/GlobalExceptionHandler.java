//package org.example.config;
//
//import org.example.dto.AppErrorDTO;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler
//    public ResponseEntity<AppErrorDTO> handleException(Exception e, HttpServletRequest req) {
//        return ResponseEntity.badRequest().body(new AppErrorDTO(req.getRequestURI(), e.getMessage(),400));
//    }
//}
