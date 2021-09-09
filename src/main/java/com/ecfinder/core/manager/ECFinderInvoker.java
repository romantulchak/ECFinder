package com.ecfinder.core.manager;

import com.ecfinder.core.ECFinderHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ECFinderInvoker<E> {

    private final ECFinderHandler<E> ecFinderHandler;

    public ECFinderInvoker(ECFinderHandler<E> ecFinderHandler) {
        this.ecFinderHandler = ecFinderHandler;
    }

    public List<E> invoke(long entityId, Class<?> clazz, Class<?> parentEntity) {
        return ecFinderHandler.handleElements(entityId, clazz, parentEntity);
    }

}
