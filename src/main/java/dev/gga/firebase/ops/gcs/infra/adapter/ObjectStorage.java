package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import dev.gga.firebase.ops.gcs.domain.dto.FolderListing;

import java.util.HashSet;
import java.util.Optional;

public interface ObjectStorage {

    HashSet<BlobId> getAllBlobInfo();

    Optional<Blob> getBlobById(final String id);

    FolderListing getBlobByPrefix(final String prefix);
}
