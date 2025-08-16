package dev.gga.firebase.ops.repository;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
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

    public HashSet<BlobId> findAllBlobInfo(){
        var names = new HashSet<>();
        return StreamSupport.stream(bucket.list().iterateAll().spliterator(), false)
                        .map(Blob::getBlobId)
                        .collect(Collectors.toCollection(HashSet::new));
    }

    public Optional<Blob> findBlobById(final String id) {
         return Optional.ofNullable(bucket.get(id));
    }

}
