package dev.gga.firebase.ops.dto;

public record FileItem(
        String objectName,
        long size,
        String contentType) {
}
