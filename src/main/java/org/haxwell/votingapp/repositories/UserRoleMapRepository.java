package org.haxwell.votingapp.repositories;

import org.springframework.data.repository.CrudRepository;

import org.haxwell.votingapp.entities.UserRoleMap;

public interface UserRoleMapRepository extends CrudRepository<UserRoleMap, Long> {

}
