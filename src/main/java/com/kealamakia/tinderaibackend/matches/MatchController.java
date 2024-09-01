package com.kealamakia.tinderaibackend.matches;

import com.kealamakia.tinderaibackend.conversations.Conversation;
import com.kealamakia.tinderaibackend.conversations.ConversationRepository;
import com.kealamakia.tinderaibackend.profiles.Profile;
import com.kealamakia.tinderaibackend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class MatchController {

  public record CreateMatchRequest(String profileId) {}

  private final ConversationRepository conversationRepository;
  private final ProfileRepository profileRepository;
  private final MatchRepository matchRepository;

  public MatchController(ConversationRepository conversationRepository, ProfileRepository profileRepository, MatchRepository matchRepository) {
    this.conversationRepository = conversationRepository;
    this.profileRepository = profileRepository;
    this.matchRepository = matchRepository;
  }


  @PostMapping("/matches")
  public Match createNewMatch(@RequestBody CreateMatchRequest request) {
    Profile profile = profileRepository.findById(request.profileId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Unable to find a profile with ID " + request.profileId()));

    // TODO make sure there are no existing conversations with this profile already
    Conversation conversation = new Conversation(
        UUID.randomUUID().toString(),
        profile.id(),
        List.of()
    );
    conversationRepository.save(conversation);

    Match match = new Match(UUID.randomUUID().toString(),profile, conversation.id());
    matchRepository.save(match);
    return match;
  }

  @GetMapping("/matches")
  public List<Match> getAllMatches() {
    return matchRepository.findAll();
  }
}
