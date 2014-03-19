package net.scholagest.business;

import net.scholagest.object.Clazz;

/**
 * Provides the methods to handle the classes. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ClazzBusinessLocal {
    /**
     * Get the class with the given id.
     * 
     * @param id Used to find the class
     * @return The class
     */
    public Clazz getClazz(final String id);

    /**
     * Create a new class.
     * 
     * @param clazz The information to store with the new class
     * @return The newly created class
     */
    public Clazz createClazz(final Clazz clazz);

    /**
     * Update a class.
     * 
     * @param clazz The class's information to store
     * @return The updated class
     */
    public Clazz saveClazz(final Clazz clazz);
}
