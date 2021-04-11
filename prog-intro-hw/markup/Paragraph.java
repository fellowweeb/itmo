package markup;

import java.util.List;

public class Paragraph implements Markdown, TexList, Html {
    private final List<MarkupElement> list;

    public Paragraph(final List<MarkupElement> list) {
        this.list = list;
    }

    @Override
    public void toHtml(StringBuilder builder) {
        list.forEach(markupElement -> markupElement.toHtml(builder));
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        list.forEach(el -> el.toMarkdown(builder));
    }

    @Override
    public void toTex(StringBuilder builder) {
        list.forEach(el -> el.toTex(builder));
    }
}
