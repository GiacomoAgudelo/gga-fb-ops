package dev.gga.firebase.ops.dto;

import java.util.List;

public record FolderListing(String prefix, List<String> subfolders, List<FileItem> files) {
}
