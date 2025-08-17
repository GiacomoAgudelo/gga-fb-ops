package dev.gga.firebase.ops.gcs.domain.dto;

public record FileItem(
        String objectName,
        long size,
        String contentType) {
}
