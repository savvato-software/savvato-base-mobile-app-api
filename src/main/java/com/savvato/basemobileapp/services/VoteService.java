package com.savvato.basemobileapp.services;

import com.savvato.basemobileapp.controllers.dto.VoteTotalResponse;
import com.savvato.basemobileapp.entities.Vote;

import java.util.List;

public interface VoteService {

	Vote castVote(long userId, long topicId, String direction);
	VoteTotalResponse getTotalVote(long topicId);
	List<Vote> getVoteByUserId(long userId, long topicId);
	List<Vote> getCurrentVotesByUserId(long userId);
}
