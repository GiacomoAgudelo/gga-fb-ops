package dev.gga.firebase.ops.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import dev.gga.firebase.ops.dto.NoteRequest;
import dev.gga.firebase.ops.entity.NoteDto;
import dev.gga.firebase.ops.mapper.NoteDtoMapper;
import dev.gga.firebase.ops.repository.FirestoreRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreNoteService {

    private static final String COLLECTION = "notes";
    private final FirestoreRepository firestoreRepository;
    private final NoteDtoMapper noteDtoMapper;

    public FirestoreNoteService(final FirestoreRepository firestoreRepository, final NoteDtoMapper noteDtoMapper) {
        this.firestoreRepository = firestoreRepository;
        this.noteDtoMapper = noteDtoMapper;
    }

    public NoteDto createNote(final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("title", noteRequest.title());
        data.put("content", noteRequest.content());
        data.put("createdAt", FieldValue.serverTimestamp());
        data.put("updatedAt", FieldValue.serverTimestamp());

        String noteId = firestoreRepository.create(COLLECTION, data);
        DocumentSnapshot documentSnapshot = firestoreRepository.get(COLLECTION, noteId);
        return noteDtoMapper.toDto(documentSnapshot);
    }

}
