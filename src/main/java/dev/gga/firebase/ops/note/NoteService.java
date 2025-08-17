package dev.gga.firebase.ops.note;

import com.google.cloud.firestore.DocumentSnapshot;
import dev.gga.firebase.ops.gcs.repository.abstraction.FirestoRepository;
import dev.gga.firebase.ops.note.entity.NoteDto;
import dev.gga.firebase.ops.note.mapper.NoteDtoMapper;
import dev.gga.firebase.ops.gcs.repository.implement.FirestoreRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class NoteService {

    private static final String COLLECTION = "notes";
    private final FirestoRepository firestoRepository;
    private final NoteDtoMapper noteDtoMapper;

    public NoteService(final FirestoRepository firestoRepository, final NoteDtoMapper noteDtoMapper) {
        this.firestoRepository = firestoRepository;
        this.noteDtoMapper = noteDtoMapper;
    }


    public NoteDto createNote(final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var data = noteDtoMapper.toMap(noteRequest);
        String noteId = firestoRepository.save(COLLECTION, data);
        DocumentSnapshot documentSnapshot = firestoRepository.findByCollectionAndId(COLLECTION, noteId);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public NoteDto updateNote(final String id, final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var map = noteDtoMapper.toMap(noteRequest);
        firestoRepository.update(COLLECTION, id, map);
        DocumentSnapshot documentSnapshot = firestoRepository.findByCollectionAndId(COLLECTION, id);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public NoteDto modifyNote(final String id, final NoteRequest noteRequest) throws ExecutionException, InterruptedException {
        var map = noteDtoMapper.toMapModify(noteRequest, id);
        firestoRepository.merge(COLLECTION, id, map);
        DocumentSnapshot documentSnapshot = firestoRepository.findByCollectionAndId(COLLECTION, id);
        return noteDtoMapper.toDto(documentSnapshot);
    }

    public void deleteNote(final String id) throws ExecutionException, InterruptedException {
        firestoRepository.delete(COLLECTION, id);
    }
}
