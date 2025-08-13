package dev.gga.firebase.ops.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteRequest(
@NotBlank String title,
String content) {}
