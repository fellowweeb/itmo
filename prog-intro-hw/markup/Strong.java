package markup;

import java.util.List;

public class Strong extends AbstractMarkupElement {
    public Strong(List<MarkupElement> list) {
        super(list);
    }

    @Override
    public String getTexTag() {
        return "textbf";
    }

    @Override
    public String getMarkdownTag() {
        return "__";
    }

    @Override
    protected String getHtmlTag() {
        return "strong";
    }
}
