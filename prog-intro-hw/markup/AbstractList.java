package markup;

import java.util.List;

public abstract class AbstractList implements TexList {
    private List<ListItem> list;

    protected AbstractList(List<ListItem> list) {
        this.list = list;
    }

    protected abstract String getEnvironment();

    @Override
    public void toTex(StringBuilder builder) {
        builder.append("\\begin{").append(getEnvironment()).append("}");
        list.forEach(el -> el.toTex(builder));
        builder.append("\\end{").append(getEnvironment()).append("}");
    }
}
