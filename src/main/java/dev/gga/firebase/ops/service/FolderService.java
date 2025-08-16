package dev.gga.firebase.ops.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.dto.FileItem;
import dev.gga.firebase.ops.dto.FolderListing;
import dev.gga.firebase.ops.repository.BucketRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FolderService {

    private final BucketRepository bucketRepository;

    public FolderService(final BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    public FolderListing listChildren(String rawPrefix) {
        String prefix = normDir(rawPrefix); // "docs/" | "" per root
        Page<Blob> page = bucketRepository.findBlobByPrefixAndCurrentDirectory(
                Storage.BlobListOption.prefix(prefix),
                Storage.BlobListOption.currentDirectory() // solo figli immediati
        );

        Set<String> subfolders = new TreeSet<>();
        List<FileItem> files = new ArrayList<>();

        for (Blob b : page.iterateAll()) {
            String name = b.getName();
            String rest = name.substring(prefix.length());
            int slash = rest.indexOf('/');
            if (slash >= 0) {
                subfolders.add(rest.substring(0, slash + 1)); // es. "2025/"
            } else {
                files.add(new FileItem(name, Optional.ofNullable(b.getSize()).orElse(0L), b.getContentType()));
            }
        }
        files.sort(Comparator.comparing(FileItem::objectName));
        return new FolderListing(prefix, new ArrayList<>(subfolders), files);
    }

    private static String normDir(String p) {
        if (p == null || p.isBlank()) return "";
        String s = p.replace('\\','/');
        if (s.startsWith("/")) s = s.substring(1);
        return s.endsWith("/") ? s : s + "/";
    }
}
