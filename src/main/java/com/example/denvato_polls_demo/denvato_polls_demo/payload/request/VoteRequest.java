package com.example.denvato_polls_demo.denvato_polls_demo.payload.request;

import javax.validation.constraints.NotNull;

public class VoteRequest
{
    @NotNull
    private Long choiceId;

    public Long getChoiceId()
    {
        return choiceId;
    }

    public void setChoiceId(Long choiceId)
    {
        this.choiceId = choiceId;
    }
}