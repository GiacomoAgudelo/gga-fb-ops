package dev.gga.firebase.ops.repository;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class BucketRepository {

    private final Bucket bucket;

    public BucketRepository(Bucket bucket) {
        this.bucket = bucket;
    }


    public Page<Blob> findBlobByPrefixAndCurrentDirectory(final Storage.BlobListOption prefix, final Storage.BlobListOption blobListOption) {
        return bucket.list(
                prefix,
                blobListOption
        );
    }
}
