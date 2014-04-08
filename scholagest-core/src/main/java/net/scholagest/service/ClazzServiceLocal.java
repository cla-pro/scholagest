package net.scholagest.service;

import java.util.List;

import net.scholagest.object.Clazz;

/**
 * Provides the methods to handle the classes. This level is responsible to
 * filter the results (elements and fields) and to return only what the
 * requesting user is allowed to see.
 * 
 * @author CLA
 * @since 0.14.0
 */
public interface ClazzServiceLocal {
    /**
     * Get the list of the classes identified by ids.
     * 
     * @param ids The ids to filter the list.
     * @return The classes list
     */
    public List<Clazz> getClasses(List<String> ids);

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
