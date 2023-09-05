package io.wisoft.wasabi.global.config.web.response;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseAspect {

    @Around("@within(org.springframework.web.bind.annotation.RestController) ||" +
            "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
    public <T>ResponseEntity<Response<T>> processCustomResponse(final ProceedingJoinPoint joinPoint) throws Throwable {
        final ResponseEntity<Response<T>> responseEntity = (ResponseEntity<Response<T>>) joinPoint.proceed();
        final Response<T> response = responseEntity.getBody();

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

}
