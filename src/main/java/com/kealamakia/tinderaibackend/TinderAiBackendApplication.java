package com.kealamakia.tinderaibackend;

import com.kealamakia.tinderaibackend.conversations.ConversationRepository;
import com.kealamakia.tinderaibackend.profiles.ProfileCreationService;
import com.kealamakia.tinderaibackend.profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ConversationRepository conversationRepository;

	@Autowired
	private ProfileCreationService profileCreationService;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	public void run(String... args) {
//		clearAllData();
//		profileCreationService.saveProfilesToDB();
		profileCreationService.createProfiles(0);
		profileCreationService.saveProfilesToDB();

	}

	private void clearAllData() {
		conversationRepository.deleteAll();
		profileRepository.deleteAll();
	}

}
