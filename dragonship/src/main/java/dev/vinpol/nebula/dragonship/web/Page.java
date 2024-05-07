package dev.vinpol.nebula.dragonship.web;

import lombok.Data;

@Data
public class Page {
    private String title;

    public Page withTitle(String title) {
        this.title = title;
        return this;
    }
}
