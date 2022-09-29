package com.savvato.basemobileapp.repositories;

import java.util.List;

import com.savvato.basemobileapp.entities.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic, Long> {

	List<Topic> findByUserId(Long userId);
}
