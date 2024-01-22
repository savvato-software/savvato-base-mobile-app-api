package com.savvato.basemobileapp.dto;

import lombok.Builder;

@Builder
public class GenericResponseDTO {

    public String responseMessage;
    public boolean responseBoolean;
    public long responseLong;
    
}
