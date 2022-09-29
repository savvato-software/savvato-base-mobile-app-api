package com.savvato.basemobileapp.controllers.dto;

public class VoteTotalResponse {
    public long topicId;
    public long votesFor;
    public long votesAgainst;

    public VoteTotalResponse(long topicId, long votesFor, long votesAgainst) {
        this.topicId = topicId;
        this.votesFor = votesFor;
        this.votesAgainst = votesAgainst;
    }
}
