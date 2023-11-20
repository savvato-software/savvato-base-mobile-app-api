package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.dto.GenericResponseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

@RestController
public class SMSChallengeCodeAPIController {

    private static final Log logger = LogFactory.getLog(SMSChallengeCodeAPIController.class);

    @Autowired
    SMSChallengeCodeService smsccs;

    @RequestMapping(value = { "/api/public/sendSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> sendSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {
        String phoneNumber = req.phoneNumber;  // assume the number we're getting is 10 digits, without the country code

        if (!phoneNumber.startsWith("0"))
            phoneNumber = "1" + phoneNumber;

        GenericResponseDTO genericResponseDTO = GenericResponseDTO.builder().responseMessage(smsccs.sendSMSChallengeCodeToPhoneNumber(phoneNumber)).build();
        logger.debug("Sent challenge code to " + phoneNumber + ". " + genericResponseDTO.responseMessage);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

    @RequestMapping(value = { "/api/public/clearSMSChallengeCodeToPhoneNumber" }, method=RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> clearSMSChallengeCode(HttpServletRequest request, Model model) {
        String phoneNumber = "1" + request.getParameter("phoneNumber");  // assume the number we're getting is 10 digits, without the country code
        smsccs.clearSMSChallengeCodeToPhoneNumber(phoneNumber);
        GenericResponseDTO genericResponseDTO = GenericResponseDTO.builder().responseMessage("ok").build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

    @RequestMapping(value = { "/api/public/isAValidSMSChallengeCode" }, method=RequestMethod.POST)
    public boolean isAValidSMSChallengeCode(@RequestBody @Valid SMSChallengeRequest req) {

        if ((req.phoneNumber == null || req.phoneNumber.equals("null")) || (req.code == null || req.code.equals("null"))) {
            throw new IllegalArgumentException("Cannot check for valid SMS challenge code with null phoneNumber or challenge code.");
        }

        return smsccs.isAValidSMSChallengeCode(req.phoneNumber, req.code);
    }

}
