package net.scholagest.utils;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**
 * Convinient class to use JPA in combination with guice.
 * 
 * {@see https://code.google.com/p/google-guice/wiki/JPA}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class PersistInitializer {
    @Inject
    public PersistInitializer(final PersistService persistService) {
        persistService.start();
    }
}
