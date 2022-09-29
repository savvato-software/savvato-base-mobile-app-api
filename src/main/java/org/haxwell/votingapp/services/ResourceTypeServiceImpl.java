package org.haxwell.votingapp.services;

import org.haxwell.votingapp.constants.ResourceTypeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeServiceImpl implements ResourceTypeService {

    @Value("${app.uploaded.user.resources.directory.root}")
    private String resourcesDirRoot;

    public String getDirectoryForResourceType(String resourceType) {
        String rtn = null;

        if (resourceType.equals(ResourceTypeConstants.RESOURCE_TYPE_PROFILE_IMAGE)) {
            rtn = resourcesDirRoot + "/profile";
        } else if (resourceType.equals(ResourceTypeConstants.RESOURCE_TYPE_TOPIC_IMAGE)) {
            rtn = resourcesDirRoot + "/topic";
        } else {
            throw new IllegalArgumentException("ResourceTypeService was passed an invalid resource type.");
        }

        return rtn;
    }

}
