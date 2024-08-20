package com.kealamakia.tinderaibackend.matches;

import com.kealamakia.tinderaibackend.profiles.Profile;

public record Match (
    String id,
    Profile profile,
    String conversationId
) { }
