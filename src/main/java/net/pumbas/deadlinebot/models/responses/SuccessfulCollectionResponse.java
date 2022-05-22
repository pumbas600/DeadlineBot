package net.pumbas.deadlinebot.models.responses;

import java.util.Collection;

import lombok.Getter;

@Getter
public class SuccessfulCollectionResponse<T> extends SuccessfulResponse<Collection<T>> {

	private final int results;

	public SuccessfulCollectionResponse(Collection<T> data) {
		super(data);
		this.results = data.size();
	}
}
