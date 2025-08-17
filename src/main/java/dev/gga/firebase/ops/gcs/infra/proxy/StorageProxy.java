package dev.gga.firebase.ops.gcs.infra.proxy;

import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.gcs.domain.dto.SignUrlRequest;
import org.springframework.stereotype.Component;

@Component
public class StorageProxy {

    private final Storage storage;

    public StorageProxy(final Storage storage) {
        this.storage = storage;
    }

    public String getSignUrl(final SignUrlRequest signUrlRequest) {
        return storage.signUrl(
                signUrlRequest.info(),
                signUrlRequest.durationUrlSigned(),
                signUrlRequest.timeUnit(),
                signUrlRequest.opts()).toString();

    }
}
