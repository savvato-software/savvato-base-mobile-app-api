package org.haxwell.votingapp.services;

import org.haxwell.votingapp.constants.Direction;
import org.haxwell.votingapp.controllers.dto.VoteTotalResponse;
import org.haxwell.votingapp.entities.Vote;
import org.haxwell.votingapp.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	VoteRepository voteRepo;

	@Autowired
	UserService userService;

	public Vote castVote(long userId, long topicId, String direction) {
		Long directionId = null;

		if (direction.toUpperCase().equals("FOR"))
			directionId = 1L;
		else if (direction.toUpperCase().equals("AGAINST"))
			directionId = 2L;
		else
			throw new IllegalArgumentException("Vote directions FOR and AGAINST are the only ones allowed. [" + direction + "]");

		Vote save = voteRepo.save(new Vote(userId, topicId, directionId));
		return save;
	}

	public VoteTotalResponse getTotalVote(long topicId) {
		long votesFor = voteRepo.countVotesFor(topicId);
		long votesAgainst = voteRepo.countVotesAgainst(topicId);

		return new VoteTotalResponse(topicId, votesFor, votesAgainst);
	}

	public List<Vote> getCurrentVotesByUserId(long userId) {

		// this was meant to return the most recent vote on each topic
		// note to self for when we start getting historical about voting

		List<Vote> rtn = voteRepo.findByUserId(userId);
		return rtn;
	}

	public List<Vote> getVoteByUserId(long userId, long topicId) {
		List<Vote> byUserIdAndTopicId = voteRepo.findByUserIdAndTopicId(userId, topicId);
		return byUserIdAndTopicId;
	}
}
