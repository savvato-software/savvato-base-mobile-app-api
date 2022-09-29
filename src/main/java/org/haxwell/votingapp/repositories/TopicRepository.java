package org.haxwell.votingapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.haxwell.votingapp.entities.Topic;

public interface TopicRepository extends CrudRepository<Topic, Long> {

	List<Topic> findByUserId(Long userId);
}
