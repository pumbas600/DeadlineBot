package net.pumbas.deadlinebot.models.responses;

import lombok.Getter;

@Getter
public class InvalidResponse extends Response {

	private final String message;

	public InvalidResponse(String message) {
		super(Status.ERROR);
		this.message = message;
	}
}
