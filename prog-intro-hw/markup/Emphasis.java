package markup;

import java.util.List;

public class Emphasis extends AbstractMarkupElement {
    public Emphasis(List<MarkupElement> list) {
        super(list);
    }

    @Override
    public String getTexTag() {
        return "emph";
    }

    @Override
    public String getMarkdownTag() {
        return "*";
    }

    @Override
    protected String getHtmlTag() {
        return "em";
    }
}
