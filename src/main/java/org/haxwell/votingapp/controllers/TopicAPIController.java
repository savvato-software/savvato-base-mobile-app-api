package org.haxwell.votingapp.controllers;

import java.time.OffsetDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.haxwell.votingapp.controllers.dto.TopicRequest;
import org.haxwell.votingapp.dto.TopicDTO;
import org.haxwell.votingapp.entities.Topic;
import org.haxwell.votingapp.services.TopicService;

@RestController
public class TopicAPIController {

	@Autowired
	TopicService topicService;
	
	TopicAPIController() {
			
	}
	
	@RequestMapping(value = { "/api/topic/create" }, method=RequestMethod.POST)
	public ResponseEntity<Topic> create(@RequestBody @Valid TopicRequest request) {

		Topic topic = topicService.create(request.userId, request.name, request.description, Long.valueOf(request.expiration));
		
		if (topic == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(topic);
		}
	}
	
	@RequestMapping(value = { "/api/topic" }, method=RequestMethod.GET)
	public ResponseEntity<List<TopicDTO>> getAll() {
		
		List<TopicDTO> list = topicService.getAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@RequestMapping(value = { "/api/user/{userId}/topics" }, method=RequestMethod.GET)
	public ResponseEntity<List<Topic>> getAllByUserId(@PathVariable Long userId) {

		List<Topic> list = topicService.getByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@RequestMapping(value = { "/api/topic/{topicId}" }, method=RequestMethod.GET)
	public ResponseEntity<Topic> getById(@PathVariable Long topicId) {
		
		Topic topic = topicService.getById(topicId);
		
		return ResponseEntity.status(HttpStatus.OK).body(topic);
	}
	
	@RequestMapping(value = { "/api/topic/{topicId}" }, method=RequestMethod.PUT)
	public ResponseEntity<Topic> update(@RequestBody @Valid TopicRequest request) {
		Topic topic = topicService.update(request.id, request.name, request.description, Long.valueOf(request.expiration));
		
		if (topic == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(topic);
		}
	}

	@RequestMapping(value = { "/api/topic/{topicId}" }, method=RequestMethod.DELETE)
	public ResponseEntity<Topic> delete(@RequestBody @Valid TopicRequest request) {
		Topic topic = topicService.delete(request.id);
		
		if (topic == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(topic);
		}
    }
}

