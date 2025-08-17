package dev.gga.firebase.ops.gcs.repository.abstraction;

import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public interface FirestoRepository {

    /**
     *
     * @param collection
     * @param data
     * @return
     */
    String save(final String collection, final Map<String, Object> data) throws ExecutionException, InterruptedException ;

    /**
     *
     * @param collection
     * @param id
     * @return
     */
    DocumentSnapshot findByCollectionAndId(final String collection, final String id) throws ExecutionException, InterruptedException ;

    /**
     *
     * @param collection
     * @param id
     * @param data
     * @return
     */
    String update(final String collection, final String id, final Map<String, Object> data) throws ExecutionException, InterruptedException ;

    /**
     *
     * @param collection
     * @param id
     * @param data
     * @return
     */
    String merge(final String collection, final String id, final Map<String, Object> data) throws ExecutionException, InterruptedException ;

    void delete(final String collection, final String id) throws ExecutionException, InterruptedException ;

}

