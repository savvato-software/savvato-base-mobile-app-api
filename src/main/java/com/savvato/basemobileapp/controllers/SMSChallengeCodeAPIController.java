package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.dto.GenericResponseDTO;
import lombok.extern.slf4j.Slf4j;
import com.savvato.basemobileapp.controllers.dto.SMSChallengeRequest;
import com.savvato.basemobileapp.services.SMSChallengeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class SMSChallengeCodeAPIController {

    @Autowired
    SMSChallengeCodeService smsccs;

    @RequestMapping(value = { "/api/public/sendSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> sendSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {
        String phoneNumber = req.phoneNumber;  // assume the number we're getting is 10 digits, without the country code

        if (!phoneNumber.startsWith("0"))
            phoneNumber = "1" + phoneNumber;
        String challengeCodeMessage = smsccs.sendSMSChallengeCodeToPhoneNumber(phoneNumber);
        GenericResponseDTO genericResponseDTO = GenericResponseDTO
                .builder()
                .responseMessage(challengeCodeMessage)
                .build();
        log.debug("Sent challenge code to " + phoneNumber + ". " + genericResponseDTO.responseMessage);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

    @RequestMapping(value = { "/api/public/clearSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> clearSMSChallengeCode(HttpServletRequest request, Model model) {
        String phoneNumber = "1" + request.getParameter("phoneNumber");  // assume the number we're getting is 10 digits, without the country code
        smsccs.clearSMSChallengeCodeToPhoneNumber(phoneNumber);
        GenericResponseDTO genericResponseDTO = GenericResponseDTO
                .builder()
                .responseMessage("ok")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

    @RequestMapping(value = { "/api/public/isAValidSMSChallengeCode" }, method=RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> isAValidSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {

        if ((req.phoneNumber == null || req.phoneNumber.equals("null")) || (req.code == null || req.code.equals("null"))) {
            throw new IllegalArgumentException("Cannot check for valid SMS challenge code with null phoneNumber or challenge code.");
        }
        Boolean isValidCode = smsccs.isAValidSMSChallengeCode(req.phoneNumber, req.code);
        GenericResponseDTO genericResponseDTO = GenericResponseDTO
                .builder()
                .responseBoolean(isValidCode)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

}
