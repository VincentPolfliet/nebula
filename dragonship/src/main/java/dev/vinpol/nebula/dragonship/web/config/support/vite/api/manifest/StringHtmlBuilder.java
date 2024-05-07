package dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest;

import j2html.Config;
import j2html.rendering.HtmlBuilder;
import j2html.rendering.IndentedHtml;
import j2html.rendering.TagBuilder;

import java.io.IOException;

public class StringHtmlBuilder implements HtmlBuilder<StringBuilder> {

    private final HtmlBuilder<StringBuilder> inner;

    public StringHtmlBuilder(HtmlBuilder<StringBuilder> inner) {
        this.inner = inner;
    }

    public static StringHtmlBuilder pretty() {
        Config config = Config.defaults()
            .withEmptyTagsClosed(true)
            .withIndenter((level, text) -> "   ".repeat(level) + text);

        return new StringHtmlBuilder(IndentedHtml.inMemory(config));
    }

    @Override
    public TagBuilder appendStartTag(String name) throws IOException {
        return inner.appendStartTag(name);
    }

    @Override
    public HtmlBuilder<StringBuilder> appendEndTag(String name) throws IOException {
        return inner.appendEndTag(name);
    }

    @Override
    public TagBuilder appendEmptyTag(String name) throws IOException {
        return inner.appendEmptyTag(name);
    }

    @Override
    public HtmlBuilder<StringBuilder> appendEscapedText(String txt) throws IOException {
        return inner.appendEscapedText(txt);
    }

    @Override
    public HtmlBuilder<StringBuilder> appendUnescapedText(String txt) throws IOException {
        return inner.appendUnescapedText(txt);
    }

    @Override
    public StringBuilder output() {
        return inner.output();
    }

    @Override
    @Deprecated
    public HtmlBuilder<StringBuilder> append(CharSequence csq) throws IOException {
        return inner.append(csq);
    }

    @Override
    @Deprecated
    public HtmlBuilder<StringBuilder> append(CharSequence csq, int start, int end) throws IOException {
        return inner.append(csq, start, end);
    }

    @Override
    @Deprecated
    public HtmlBuilder<StringBuilder> append(char c) throws IOException {
        return inner.append(c);
    }

    @Override
    public String toString() {
        return output().toString();
    }
}
