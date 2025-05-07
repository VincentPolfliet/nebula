package dev.vinpol.spacetraders.sdk.utils.page;

/**
 * Represents a request for a specific page of data.
 *
 * @param page the page number to retrieve (1-based index)
 * @param size the maximum number of items per page
 */
public record PageRequest(int page, int size) {

}
