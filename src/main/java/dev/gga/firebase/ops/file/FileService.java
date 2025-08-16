package dev.gga.firebase.ops.file;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.gcs.infra.adapter.FolderOps;
import dev.gga.firebase.ops.gcs.infra.adapter.ObjectStorage;
import dev.gga.firebase.ops.repository.BucketRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {

    private final ObjectStorage objectStorage;

    private final FolderOps folderOps;

    public FileService(final ObjectStorage objectStorage, final FolderOps folderOps) {
        this.objectStorage = objectStorage;
        this.folderOps = folderOps;
    }

    public HashSet<BlobId> retriveAllBlobInfo(){
        return objectStorage.getAllBlobInfo();
    }

    public Optional<FileDto> retriveFile(final String id) {
        var optionaBlob = objectStorage.getBlobById(id);
        if(optionaBlob.isEmpty()){
            return Optional.empty();
        }
        var file =  optionaBlob.get();
        var streamingResponseBody = this.buildStreamingResponseBody(file);
        return Optional.of(new FileDto(id, file, streamingResponseBody, null));
    }

    public Optional<FileDto> retriveSignUrl(final String id) {
        Optional<String> optionalUrl = folderOps.getSignUrl(id);
        if(optionalUrl.isEmpty()){
            return Optional.empty();
        }
        var url = optionalUrl.get();
        return Optional.of(new FileDto(null, null, null, url));
    }

    private StreamingResponseBody buildStreamingResponseBody(final Blob file) {
        StreamingResponseBody body = output -> {
            try (ReadChannel reader = file.reader()) {
                ByteBuffer buf = ByteBuffer.allocate(1024 * 1024);
                int read;
                while ((read = reader.read(buf)) > 0) {
                    output.write(buf.array(), 0, read);
                    buf.clear();
                }
                output.flush();
            }
        };
        return body;
    }
}
