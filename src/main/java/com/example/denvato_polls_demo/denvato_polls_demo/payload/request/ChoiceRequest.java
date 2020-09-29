package com.example.denvato_polls_demo.denvato_polls_demo.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChoiceRequest
{
    @NotBlank
    @Size(max = 40)
    private String text;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}