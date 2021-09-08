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

    public List<E> invoke(long entityId, String table, Class<?> clazz) {
        return ecFinderHandler.handleElements(entityId, table, clazz);
    }

}
