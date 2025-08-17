package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.cloud.storage.*;
import dev.gga.firebase.ops.gcs.domain.dto.FileItem;
import dev.gga.firebase.ops.gcs.domain.dto.FolderListing;
import dev.gga.firebase.ops.gcs.infra.mapper.BlobMapper;
import dev.gga.firebase.ops.gcs.infra.mapper.StorageMapper;
import dev.gga.firebase.ops.gcs.infra.proxy.BucketProxy;
import dev.gga.firebase.ops.gcs.infra.proxy.StorageProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GcsStorageFacade implements BucketAdmin, FolderOps, ObjectStorage {

    private final BucketProxy bucketProxy;

    private final StorageProxy storageProxy;

    private final StorageMapper storageMapper;

    private final BlobMapper blobMapper;

    @Value("${app.firebase.durationUrlSigned}")
    private int durationUrlSigned;

    private static final String HEADER_CONTENT = "response-content-disposition";


    public GcsStorageFacade(final StorageProxy storageProxy, final BucketProxy bucketProxy,
                            final StorageMapper storageMapper, final BlobMapper blobMapper) {
        this.bucketProxy = bucketProxy;
        this.storageProxy = storageProxy;
        this.storageMapper = storageMapper;
        this.blobMapper = blobMapper;
    }

    @Override
    public HashSet<BlobId> getAllBlobInfo() {
        return bucketProxy.getAllBlobInfo();
    }

    @Override
    public Optional<Blob> getBlobById(final String id) {
        return bucketProxy.getBlobById(id);
    }

    @Override
    public FolderListing getBlobByPrefix(final String prefix) {
        var page = bucketProxy.getBlobByPrefix(prefix);
        Set<String> subfolders = new TreeSet<>();
        List<FileItem> files = new ArrayList<>();

        for (Blob blob : page.iterateAll()) {
            this.groupImmediateChildren(prefix, blob, subfolders, files);
        }

        files.sort(Comparator.comparing(FileItem::objectName));
        return blobMapper.toFolderListing(prefix, subfolders, files);
    }

    private void groupImmediateChildren(final String prefix, final Blob blob, final Set<String> subfolders, final List<FileItem> files) {
        String name = blob.getName();
        String rest = name.substring(prefix.length());
        int slash = rest.indexOf('/');
        if (slash >= 0) {
            subfolders.add(rest.substring(0, slash + 1));
        } else {
            files.add(blobMapper.toFileItem(blob));
        }
    }

    @Override
    public Optional<String> getSignUrl(final String id) {

        var optionaBlob = bucketProxy.getBlobById(id);
        if(optionaBlob.isEmpty()){
            return Optional.empty();
        }
        var file =  optionaBlob.get();
        Map<String,String> qp = new HashMap<>();
        qp.put(HEADER_CONTENT, "inline" + "; filename=\"" + id + "\"");

        var signUrlRequest = storageMapper.toSignUrlRequest(file, qp,
                durationUrlSigned,
                java.util.concurrent.TimeUnit.MINUTES);

        var url = storageProxy.getSignUrl(signUrlRequest);

        return Optional.ofNullable(url);
    }
}
