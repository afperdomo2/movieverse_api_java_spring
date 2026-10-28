package com.afperdomo2.movieverse.common.uuid;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

/**
 * Genera identificadores UUID v7 (RFC 9562), ordenables por fecha de creación.
 */
public final class UuidV7Generator {

    private UuidV7Generator() {
    }

    public static UUID generate() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
