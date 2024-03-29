package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.constants.ResourceTypeConstants;
import com.savvato.basemobileapp.dto.GenericResponseDTO;
import com.savvato.basemobileapp.services.PictureService;
import com.savvato.basemobileapp.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
public class FileUploadController {

	private final StorageService storageService;
	private final PictureService pictureService;

	@Autowired
	public FileUploadController(StorageService storageService, PictureService pictureService) {
		this.storageService = storageService;
		this.pictureService = pictureService;
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}/isFound" }, method = RequestMethod.GET)
	public ResponseEntity<GenericResponseDTO> fileIsFound(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId) {
		long timestamp = 0;

		if (isValidResourceType(resourceType)) {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			timestamp = storageService.isFileExisting(resourceType, filename);
		}
		GenericResponseDTO genericResponseDTO = GenericResponseDTO
				.builder()
				.responseLong(timestamp)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.DELETE)
	public ResponseEntity<GenericResponseDTO> deleteFile(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId) {
		boolean b = false;

		if (isValidResourceType(resourceType)) {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			b = storageService.delete(resourceType, filename);
		}
		GenericResponseDTO genericResponseDTO = GenericResponseDTO
				.builder()
				.responseBoolean(b)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> serveFile(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId, @RequestParam("photoSize") String photoSize) {
		String filename = storageService.getDefaultFilename(resourceType, resourceId);
		filename = pictureService.transformFilenameUsingSizeInfo(photoSize, filename);

		log.debug("^^^^^ About to call storageservice to load --> " + filename);
		byte[] fileAsByteArray = storageService.loadAsByteArray(resourceType, filename);

		log.debug("^^^^^^ Back from storageservice call to load --> " + filename);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileAsByteArray);
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.POST)
	public ResponseEntity<GenericResponseDTO> handleFileUpload(HttpServletRequest request, @PathVariable String resourceType,
											   @PathVariable String resourceId, @RequestParam("file") MultipartFile file) {

		GenericResponseDTO genericResponseDTO = GenericResponseDTO.builder().build();

		if (!isValidResourceType(resourceType)) {
			// Return a 400 Bad Request if the resource type is invalid
			log.debug("^^^^^ COULD NOT do the HandleFileUpload!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		try {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			log.debug("^^^^ About to call storage service to save --> " + filename);
			storageService.store(resourceType, file, filename);

			log.debug("^^^^ About to call pictureservice to write thumbnail --> " + filename);
			pictureService.writeThumbnailFromOriginal(resourceType, filename);
			genericResponseDTO.responseMessage = "ok";

			// Return success response with 200 OK
			return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);

		} catch (IOException ioe){
			// Handle IO exceptions and return a 500 Internal Server Error
			genericResponseDTO.responseMessage = "error";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponseDTO);
		}

	}

	private boolean isValidResourceType(String resourceType) {
		return resourceType.equals(ResourceTypeConstants.RESOURCE_TYPE_PROFILE_IMAGE);
	}
}
