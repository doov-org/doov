package io.doov.core.dsl.runtime;

/**
 * Building block for the runtime implementation.
 * PathMethod implements actions on a field : get/set/create
 *
 * @param <T> path container type
 * @param <R> field return type
 */
public interface PathMethod<T, R> {

    /**
     * Get value
     *
     * @param link path container
     * @return field value
     */
    R get(T link);

    /**
     * Set value
     *
     * @param link  path container
     * @param value value to set
     */
    void set(T link, R value);

    /**
     * Create, set in the path container and return the value
     *
     * @param link path container
     * @return created value
     */
    R create(T link);

}
