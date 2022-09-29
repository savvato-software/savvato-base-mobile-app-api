package com.savvato.basemobileapp.services;

import java.util.List;

import com.savvato.basemobileapp.dto.TopicDTO;
import com.savvato.basemobileapp.entities.Topic;

public interface TopicService {
	public Topic getById(Long id);

	public List<TopicDTO> getAll();

	public Topic create(Long userId, String name, String description, long expirationInMillis);

	public List<Topic> getByUserId(Long userId);

	public Topic update(Long id, String name, String description, long expirationInMillis);

    public Topic delete(Long id);
}

