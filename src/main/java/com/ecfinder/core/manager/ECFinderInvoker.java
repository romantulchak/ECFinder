package com.ecfinder.core.manager;

import com.ecfinder.core.ECFinderHandler;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

import java.util.List;

@Component
public class ECFinderInvoker<E> {

    private final ECFinderHandler<E> ecFinderHandler;

    public ECFinderInvoker(ECFinderHandler<E> ecFinderHandler) {
        this.ecFinderHandler = ecFinderHandler;
    }

    /**
     * Check if Embeddable class and Parent Entity marked with the necessary annotations
     * gets parent column id name and table name and it makes a query to the database
     *
     * @param entityId     Depending on it the search will take place
     * @param clazz        Embeddable class
     * @param parentEntity Parent entity class
     * @return List of elements depending on Generic type
     */
    public List<E> invoke(long entityId, Class<?> clazz, Class<?> parentEntity) {
        return ecFinderHandler.handleElements(entityId, clazz, parentEntity);
    }

    /**
     * Gets Page<E> depends on Pageable
     *
     * @param entityId Depending on it the search will take place
     * @param clazz Embeddable class
     * @param parentEntity Parent entity class
     * @param pageable Pageable object that contains current page, elements per page
     * @return Page object that contains content, pageable and total elements
     */
    public Page<E> invoke(long entityId, Class<?> clazz, Class<?> parentEntity, Pageable pageable){
        return ecFinderHandler.handleElements(entityId, clazz, parentEntity, pageable);
    }

    /**
     * Gets single object by unique column
     *
     * @param entityId Depending on it the search will take place
     * @param uniqueValue Unique value by which the search will be performed
     * @param clazz Embeddable class
     * @param parentEntity Parent entity class
     * @return single object by unique column
     */
    public <U> E invoke(long entityId, U uniqueValue,Class<?> clazz, Class<?> parentEntity){
        return ecFinderHandler.handleElements(entityId, uniqueValue, clazz, parentEntity);
    }
}
