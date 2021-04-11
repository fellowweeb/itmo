package markup;

import java.util.List;

public class Strikeout extends AbstractMarkupElement {
    public Strikeout(List<MarkupElement> list) {
        super(list);
    }

    @Override
    public String getTexTag() {
        return "textst";
    }

    @Override
    public String getMarkdownTag() {
        return "~";
    }

    @Override
    protected String getHtmlTag() {
        return "s";
    }
}
