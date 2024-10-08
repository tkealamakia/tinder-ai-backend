package com.kealamakia.tinderaibackend.conversations;

import com.kealamakia.tinderaibackend.profiles.Profile;
import com.kealamakia.tinderaibackend.profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class ConversationController {

  private final ConversationRepository conversationRepository;
  private final ProfileRepository profileRepository;
  private final ConversationService conversationService;

  public ConversationController(ConversationRepository conversationRepository,
                                ProfileRepository profileRepository,
                                ConversationService conversationService) {
    this.conversationRepository = conversationRepository;
    this.profileRepository = profileRepository;
    this.conversationService = conversationService;
  }

  @CrossOrigin(origins = "*")
  @PostMapping("/conversations")
  public Conversation createNewConversation(@RequestBody CreateConverstationRequest request) {
    profileRepository.findById(request.profileId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Unable to find a profile with ID " + request.profileId()));
    Conversation conversation = new Conversation(
        UUID.randomUUID().toString(),
        request.profileId(),
        List.of()
    );
    conversationRepository.save(conversation);
    return conversation;
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/conversations/{conversationId}")
  public Conversation getConversation(@PathVariable String conversationId) {
    Conversation conversation = conversationRepository.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Unable to find a conversation with ID " + conversationId));
    return conversation;
  }

  @CrossOrigin(origins = "*")
  @PostMapping("/conversations/{conversationId}")
  public Conversation addMessageToConversation(@PathVariable String conversationId,
                                               @RequestBody ChatMessage chatMessage) {
    Conversation conversation = conversationRepository.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Unable to find a conversation with ID " + conversationId));
    String matchProfileId = conversation.profileId();
    Profile profile = profileRepository.findById(matchProfileId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Unable to find a profile with ID " + chatMessage.authorId()));
    Profile user = profileRepository.findById(chatMessage.authorId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Unable to find a profile with ID " + chatMessage.authorId()
        ));

    // TODO Need to validate that the author of a message happens to only be the
    ChatMessage messageWithTime = new ChatMessage(
        chatMessage.messageText(),
        chatMessage.authorId(),
        LocalDateTime.now()
    );
    conversation.messages().add(messageWithTime);
    conversationService.generateProfileResponse(conversation, profile, user);
    conversationRepository.save(conversation);
    return conversation;
  }

  public record CreateConverstationRequest(
      String profileId
  ) {}

  @DeleteMapping("/delete-conversations")
  public void deleteAllConversations() {
    conversationRepository.deleteAll();
  }
}

