package dev.gga.firebase.ops.controller;

import com.google.cloud.storage.Bucket;
import dev.gga.firebase.ops.dto.ZipRequest;
import dev.gga.firebase.ops.service.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/v1/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping(produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> createZip(@RequestBody ZipRequest req){
        return ResponseEntity.ok(archiveService.createZip());
    }


}
