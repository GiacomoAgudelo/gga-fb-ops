package dev.gga.firebase.ops.folder;

import dev.gga.firebase.ops.gcs.infra.adapter.ObjectStorage;
import dev.gga.firebase.ops.gcs.domain.dto.FolderListing;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    private final ObjectStorage objectStorage;

    public FolderService(final ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }


    public FolderListing listChildren(String rawPrefix) {
        String prefix = normDir(rawPrefix);
        return objectStorage.getBlobByPrefix(prefix);
    }

    private static String normDir(String p) {
        if (p == null || p.isBlank()) return "";
        String s = p.replace('\\','/');
        if (s.startsWith("/")) s = s.substring(1);
        return s.endsWith("/") ? s : s + "/";
    }
}
