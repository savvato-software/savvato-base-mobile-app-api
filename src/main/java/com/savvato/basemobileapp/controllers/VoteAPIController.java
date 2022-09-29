package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.controllers.dto.VoteRequest;
import com.savvato.basemobileapp.controllers.dto.VoteTotalResponse;
import com.savvato.basemobileapp.entities.Vote;
import com.savvato.basemobileapp.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class VoteAPIController {

	@Autowired
    VoteService voteService;

	VoteAPIController() {

	}

	@RequestMapping(value = {"/api/vote"}, method = RequestMethod.POST)
	public ResponseEntity<Vote> castVote(@RequestBody @Valid VoteRequest request) {

		Vote vote = voteService.castVote(request.userId, request.topicId, request.direction);

		if (vote == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(vote);
		}
	}

	@RequestMapping(value = { "/api/vote/{topicId}" }, method = RequestMethod.GET)
	public ResponseEntity<VoteTotalResponse> getTotalVote(@PathVariable long topicId) {
		VoteTotalResponse rtn = voteService.getTotalVote(topicId);

		return ResponseEntity.status(HttpStatus.OK).body(rtn);
	}

	@RequestMapping(value = { "/api/vote/user/{userId}" }, method = RequestMethod.GET)
	public ResponseEntity<List<Vote>> getVoteByUserId(@PathVariable long userId) {
		List<Vote> rtn = voteService.getCurrentVotesByUserId(userId);

		return ResponseEntity.status(HttpStatus.OK).body(rtn);
	}

	@RequestMapping(value = { "/api/vote/{topicId}/user/{userId}" }, method = RequestMethod.GET)
	public ResponseEntity<List<Vote>> getVoteByUserId(@PathVariable long topicId, @PathVariable long userId) {
		List<Vote> rtn = voteService.getVoteByUserId(userId, topicId);

		return ResponseEntity.status(HttpStatus.OK).body(rtn);
	}
}
