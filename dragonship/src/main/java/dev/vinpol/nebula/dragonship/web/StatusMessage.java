package dev.vinpol.nebula.dragonship.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusMessage(@JsonProperty("message") String message) {
}
