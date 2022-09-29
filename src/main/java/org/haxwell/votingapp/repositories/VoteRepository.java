package org.haxwell.votingapp.repositories;

import org.haxwell.votingapp.entities.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(nativeQuery = true, value = "select count(*) from vote where topic_id=?1 and direction=1")
    long countVotesFor(long topicId);

    @Query(nativeQuery = true, value = "select count(*) from vote where topic_id=?1 and direction=2")
    long countVotesAgainst(long topicId);

    List<Vote> findByUserIdAndTopicId(long userId, long topicId);

    List<Vote> findByUserId(long userId);
}
