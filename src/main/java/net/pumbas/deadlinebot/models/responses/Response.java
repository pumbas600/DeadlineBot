package net.pumbas.deadlinebot.models.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response {
	public enum Status {
		SUCCESSFUL, ERROR
	}

	private final Status status;

	public static ResponseEntity<Response> error(int status, String message) {
		return ResponseEntity.status(status).body(new InvalidResponse(message));
	}

	public static <T> ResponseEntity<Response> success(T data) {
		return ResponseEntity.ok(new SuccessfulResponse<>(data));
	}

	public static <T> ResponseEntity<Response> success(Collection<T> data) {
		return ResponseEntity.ok(new SuccessfulCollectionResponse<>(data));
	}

	public static ResponseEntity<Response> success() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessfulResponse<>(null));
	}
}
