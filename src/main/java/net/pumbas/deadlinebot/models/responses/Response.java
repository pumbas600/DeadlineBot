package net.pumbas.deadlinebot.models.responses;

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

	public static Response error(String message) {
		return new InvalidResponse(message);
	}

	public static <T> Response success(T data) {
		return new SuccessfulResponse<>(data);
	}

	public static <T> Response success(Collection<T> data) {
		return new SuccessfulCollectionResponse<>(data);
	}
}
