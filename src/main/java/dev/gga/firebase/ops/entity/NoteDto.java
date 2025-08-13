package dev.gga.firebase.ops.entity;

import java.time.Instant;

public record NoteDto(String id,
   String title,
   String content,
   Instant createdAt,
   Instant updatedAt
) {}
