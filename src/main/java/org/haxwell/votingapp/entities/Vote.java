package org.haxwell.votingapp.entities;

import org.haxwell.votingapp.constants.Direction;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@IdClass(VoteId.class)
public class Vote {

	private static final long serialVersionUID = 133290921L;

	@Id
	private Long userId;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Id
	private Long topicId;

	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long id) {
		this.topicId = id;
	}

	private Long direction;

	public Long getDirection() {
		return this.direction;
	}

	public void setDirection(Long id) {
		this.direction = id;
	}

	///
	private java.sql.Timestamp created;

    public java.sql.Timestamp getCreated() {
        return created;
    }

    public void setCreated() {
    	this.created = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public void setCreated(java.sql.Timestamp ts) {
    	this.created = ts;
    }

    ///
    private java.sql.Timestamp lastUpdated;

    public java.sql.Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated() {
    	this.lastUpdated = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public void setLastUpdated(java.sql.Timestamp ts) {
    	this.lastUpdated = ts;
    }

    /////
	public Vote(Long userId, Long topicId, Long directionId) {
		this.userId = userId;
		this.topicId = topicId;
		this.direction = directionId;

		if (this.direction < 1L)
			this.direction = 1L;

		if (this.direction > 2L)
			this.direction = 2L;

		setCreated();
		setLastUpdated();
	}

	public Vote() {

	}
}
