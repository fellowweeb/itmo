package markup;

import java.util.List;

public abstract class AbstractMarkupElement implements MarkupElement {
    private List<MarkupElement> list;

    protected AbstractMarkupElement(List<MarkupElement> list) {
        this.list = list;
    }

    protected abstract String getMarkdownTag();

    protected abstract String getTexTag();

    protected abstract String getHtmlTag();

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(getMarkdownTag());
        list.forEach(el -> el.toMarkdown(builder));
        builder.append(getMarkdownTag());
    }

    @Override
    public void toTex(StringBuilder builder) {
        builder.append("\\").append(getTexTag()).append("{");
        list.forEach(el -> el.toTex(builder));
        builder.append("}");
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append("<").append(getHtmlTag()).append(">");
        list.forEach(el -> el.toHtml(builder));
        builder.append("</").append(getHtmlTag()).append(">");
    }
}
