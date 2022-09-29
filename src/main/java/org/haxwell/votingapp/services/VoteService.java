package org.haxwell.votingapp.services;

import org.haxwell.votingapp.constants.Direction;
import org.haxwell.votingapp.controllers.dto.VoteTotalResponse;
import org.haxwell.votingapp.entities.Vote;

import java.util.List;

public interface VoteService {

	Vote castVote(long userId, long topicId, String direction);
	VoteTotalResponse getTotalVote(long topicId);
	List<Vote> getVoteByUserId(long userId, long topicId);
	List<Vote> getCurrentVotesByUserId(long userId);
}
