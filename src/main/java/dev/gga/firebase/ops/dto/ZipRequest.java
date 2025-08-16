package dev.gga.firebase.ops.dto;

import java.util.HashSet;

public record ZipRequest(
        HashSet<String> paths,
        boolean preservePaths,
        String downloadName
) {
}
