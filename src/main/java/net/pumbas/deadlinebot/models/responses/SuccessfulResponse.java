package net.pumbas.deadlinebot.models.responses;

import lombok.Getter;

@Getter
public class SuccessfulResponse<T> extends Response {

	private final T data;

	public SuccessfulResponse(T data) {
		super(Status.SUCCESSFUL);
		this.data = data;
	}
}
