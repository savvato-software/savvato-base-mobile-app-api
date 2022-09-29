package com.savvato.basemobileapp.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savvato.basemobileapp.dto.TopicDTO;
import com.savvato.basemobileapp.entities.Topic;
import com.savvato.basemobileapp.entities.User;
import com.savvato.basemobileapp.repositories.TopicRepository;

@Service
public class TopicServiceImpl implements TopicService {

	@Autowired
	TopicRepository topicRepo;

	@Autowired
	UserService userService;

//	@Autowired
//	UserRoleMapService userRoleMapService;


	public Topic create(Long userId, String name, String description, long expirationInMillis) {
		Topic entity = new Topic(userId, name, description, expirationInMillis);

		Topic rtn = topicRepo.save(entity);

//		userRoleMapService.addRoleToUser(userId, UserRoleMapService.ROLES.IDE ATOR);

		return rtn;
	}

	public List<Topic> getByUserId(Long userId) {
		return topicRepo.findByUserId(userId);
	}

	public List<TopicDTO> getAll() {
		Iterable<Topic> iterable = topicRepo.findAll();

		Iterator<Topic> iterator = iterable.iterator();

		List<TopicDTO> list = new ArrayList<>();

		while (iterator.hasNext()) {
			Topic topic = iterator.next();

			// TODO.. some caching of some sort is necessary here.. to keep from reading the database so many times.
			Optional<User> opt = userService.findById(topic.getUserId());
			User u = opt.get();

			TopicDTO rtn = new TopicDTO();
			rtn.topic = topic;
			rtn.userId = u.getId();
			rtn.userName = u.getName();

			list.add(rtn);
		}

		return list;
	}

	public Topic getById(Long id) {
		Optional<Topic> opt = topicRepo.findById(id);

		if (opt.isPresent())
			return opt.get();
		else
			return null;
	}

	public Topic update(Long id, String name, String description, long expirationInMillis) {
		Optional<Topic> opt = topicRepo.findById(id);

		if (opt.isPresent()) {

			Topic topic = opt.get();

			topic.setName(name);
			topic.setDescription(description);
			topic.setExpiration(expirationInMillis);

			return topicRepo.save(topic);
		} else {
			return null;
		}
	}

	public Topic delete(Long id) {

	    // TODO
        return null;
	}
}
