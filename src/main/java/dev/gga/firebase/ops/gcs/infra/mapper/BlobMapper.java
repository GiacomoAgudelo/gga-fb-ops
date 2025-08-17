package dev.gga.firebase.ops.gcs.infra.mapper;

import com.google.cloud.storage.Blob;
import dev.gga.firebase.ops.gcs.domain.dto.FileItem;
import dev.gga.firebase.ops.gcs.domain.dto.FolderListing;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class BlobMapper {

    public FileItem toFileItem(final Blob blob) {
        return new FileItem(blob.getName(), Optional.ofNullable(blob.getSize()).orElse(0L), blob.getContentType());
    }

    public FolderListing toFolderListing(final String prefix, final Set<String> subfolders, final List<FileItem> files) {
        return new FolderListing(prefix, new ArrayList<>(subfolders), files);
    }
}
