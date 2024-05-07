package dev.vinpol.spacetraders.sdk.models;

import java.util.Collection;

public interface ApiPageable<T> {

    Collection<T> getData();

    Meta getMeta();
}
