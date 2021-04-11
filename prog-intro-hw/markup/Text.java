package markup;

public class Text implements MarkupElement {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder builder) {
        builder.append(text);
    }

    @Override
    public void toTex(StringBuilder builder) {
        builder.append(text);
    }

    @Override
    public void toHtml(StringBuilder builder) {
        builder.append(text);
    }
}
