package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.config.principal.UserPrincipal;
import com.savvato.basemobileapp.constants.Constants;
import com.savvato.basemobileapp.dto.ProfileDTO;
import com.savvato.basemobileapp.entities.User;
import com.savvato.basemobileapp.entities.UserRole;
import com.savvato.basemobileapp.services.*;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileAPIController.class)
public class ProfileAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceBASEMOBILEAPP userDetailsServiceBASEMOBILEAPP;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before("")
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testProfileHappyPath() throws Exception {

        // TODO: Move these constants to their own classes, and reference them here.
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);

        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("admin"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);

        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
                new UserPrincipal(user)
        );

        Mockito.when(profileService.getByUserId(Mockito.anyLong())).thenReturn(
                Optional.of(ProfileDTO.builder()
                        .name(Constants.FAKE_USER_NAME1)
                        .email(Constants.FAKE_USER_EMAIL1)
                        .phone(Constants.FAKE_USER_PHONE1)
                        .build())
        );

        String auth = AuthServiceImpl.generateAccessToken(user);

        this.mockMvc.
                perform(
                    get("/api/profile/1")
                    .header("Authorization", "Bearer " + auth)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(Constants.FAKE_USER_NAME1));
    }
}
