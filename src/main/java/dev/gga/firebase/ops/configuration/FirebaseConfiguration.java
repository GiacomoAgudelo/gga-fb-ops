package dev.gga.firebase.ops.configuration;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseConfiguration {

    private static String normalizePk(final String pk) {
        return pk == null ? null : pk.replace("\\n", "\n");
    }

    @Bean
    public FirebaseApp firebaseApp(final FirebaseProperties p) throws Exception {

        var creds = ServiceAccountCredentials.fromPkcs8(
                        p.clientId(),
                        p.clientEmail(),
                        normalizePk(p.privateKey()),
                        p.privateKeyId(),
                        p.scopes()
                ).toBuilder()
                .setTokenServerUri(URI.create(p.tokenUri()))
                .build();

        var options = FirebaseOptions.builder()
                .setCredentials(creds)
                .setProjectId(p.projectId())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Firestore firestore(final FirebaseApp app) {
        return FirestoreClient.getFirestore(app);
    }
}