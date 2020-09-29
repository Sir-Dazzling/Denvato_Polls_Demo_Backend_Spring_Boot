package com.example.denvato_polls_demo.denvato_polls_demo.controller;

import com.example.denvato_polls_demo.denvato_polls_demo.exception.ResourceNotFoundException;
import com.example.denvato_polls_demo.denvato_polls_demo.models.User;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.response.*;
import com.example.denvato_polls_demo.denvato_polls_demo.repository.PollRepository;
import com.example.denvato_polls_demo.denvato_polls_demo.repository.UserRepository;
import com.example.denvato_polls_demo.denvato_polls_demo.repository.VoteRepository;
import com.example.denvato_polls_demo.denvato_polls_demo.security.CurrentUser;
import com.example.denvato_polls_demo.denvato_polls_demo.security.UserPrincipal;
import com.example.denvato_polls_demo.denvato_polls_demo.service.PollsService;
import com.example.denvato_polls_demo.denvato_polls_demo.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollsService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//    To get logged in User details
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser)
    {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

//    To check if username is available
    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username)
    {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

//    To check if email already exists
    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email)
    {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

//    To get user profile details
    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);

        return userProfile;
    }

//    To get polls createdby a specific user
    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)
    {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

//    To get votes by list of users
    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)
    {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }
}