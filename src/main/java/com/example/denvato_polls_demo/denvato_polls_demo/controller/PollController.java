package com.example.denvato_polls_demo.denvato_polls_demo.controller;

import com.example.denvato_polls_demo.denvato_polls_demo.models.Poll;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.request.PollRequest;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.request.VoteRequest;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.response.ApiResponse;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.response.PagedResponse;
import com.example.denvato_polls_demo.denvato_polls_demo.payload.response.PollResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/polls")
public class PollController
{
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollsService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getAllPolls(currentUser, page, size);
    }

    //    Enabling User to create poll(s)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest)
    {
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }

    @GetMapping("/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId) {
        return pollService.getPollById(pollId, currentUser);
    }

    //    Enabling user to cast vote
    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long pollId,
                                 @Valid @RequestBody VoteRequest voteRequest)
    {
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    }
}