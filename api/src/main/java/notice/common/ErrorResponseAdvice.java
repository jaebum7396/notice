package notice.common;

import common.model.Response;
import io.jsonwebtoken.ExpiredJwtException;
import notice.common.exception.InvalidNoticeTimeException;
import notice.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorResponseAdvice {
	//private Logger logger = LoggerFactory.getLogger(ErrorResponseAdvice.class);
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleException(Exception e) {
		Response responseResult;
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        responseResult = Response.builder()
                .message("서버쪽 오류가 발생했습니다. 관리자에게 문의하십시오")
                .result(resultMap).build();
        return ResponseEntity.internalServerError().body(responseResult);
	}
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Response>  handleBadCredentialsException(BadCredentialsException e) {
		Response responseResult;
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        responseResult = Response.builder()
                .message("인증되지 않은 사용자입니다.")
                .result(resultMap).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseResult);
	}
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Response>  handleExpiredJwtException(ExpiredJwtException e) {
		Response responseResult;
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		responseResult = Response.builder()
				.message(e.getMessage())
				.result(resultMap).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseResult);
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		Response responseResult;
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		responseResult = Response.builder()
				.message("올바른 요청 포맷이 아닙니다.")
				.result(resultMap).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseResult);
	}
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Response> handleNotFoundException(NotFoundException e) {
		Response responseResult;
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		responseResult = Response.builder()
				.message(e.getMessage())
				.result(resultMap).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseResult);
	}
	@ExceptionHandler(InvalidNoticeTimeException.class)
	public ResponseEntity<Response> handleInvalidNoticeTime(InvalidNoticeTimeException e) {
		Response responseResult;
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		responseResult = Response.builder()
				.message(e.getMessage())
				.result(resultMap).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseResult);
	}
}
