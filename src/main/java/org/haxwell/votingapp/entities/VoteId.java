package org.haxwell.votingapp.entities;

import javax.persistence.Column;
import java.io.Serializable;

public class VoteId implements Serializable {

    private static final long serialVersionUID = 36330025L;

    @Column(name="USER_ID")
    public long userId;

    @Column(name="TOPIC_ID")
    public long topicId;

    public boolean equals(Object o) {
        boolean rtn = false;

        if (o instanceof VoteId) {
            VoteId other = (VoteId)o;

            rtn = this.userId == other.userId && this.topicId == other.topicId;
        }

        return rtn;
    }

    public int hashCode() { return Long.getLong("" + (userId + topicId)).hashCode(); }
}
