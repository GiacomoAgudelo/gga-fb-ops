package dev.gga.firebase.ops.service;

import com.google.cloud.storage.Storage;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import dev.gga.firebase.ops.dto.FileDto;
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

    private final BucketRepository bucketRepository;

    private final Storage storage;

    public FileService(final BucketRepository bucketRepository, final Storage storage) {
        this.bucketRepository = bucketRepository;
        this.storage = storage;
    }

    public HashSet<BlobId> retriveAllBlobInfo(){
        return bucketRepository.findAllBlobInfo();
    }

    public Optional<FileDto> retriveFile(final String id) {
        var optionaBlob = bucketRepository.findBlobById(id);
        if(optionaBlob.isEmpty()){
            return Optional.empty();
        }
        var file =  optionaBlob.get();
        var streamingResponseBody = this.buildStreamingResponseBody(file);
        return Optional.of(new FileDto(id, file, streamingResponseBody, null));
    }

    public Optional<FileDto> retriveSignUrl(final String id) {
        var optionaBlob = bucketRepository.findBlobById(id);
        if(optionaBlob.isEmpty()){
            return Optional.empty();
        }
        var file =  optionaBlob.get();
        BlobInfo info = BlobInfo.newBuilder(file.getBlobId()).build();
        Map<String,String> qp = new HashMap<>();
        qp.put("response-content-disposition", "inline" + "; filename=\"" + id + "\"");

        var url = storage.signUrl(
                info,
                15, java.util.concurrent.TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(com.google.cloud.storage.HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withQueryParams(qp)
        ).toString();
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
