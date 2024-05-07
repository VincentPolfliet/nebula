package dev.vinpol.nebula.dragonship.web.config.support.jte;

import gg.jte.Content;
import gg.jte.TemplateOutput;

public class RawContent implements Content {

    private final String content;

    public RawContent(String content) {
        this.content = content;
    }

    public static Content raw(String content) {
        return new RawContent(content);
    }

    @Override
    public void writeTo(TemplateOutput output) {
        output.writeContent(content);
    }

    @Override
    public boolean isEmptyContent() {
        return content == null || content.isEmpty();
    }
}
