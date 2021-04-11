package markup;

import java.util.List;

public class ListItem implements Tex {
    private List<TexList> list;

    public ListItem(List<TexList> list) {
        this.list = list;
    }

    @Override
    public void toTex(StringBuilder builder) {
        builder.append("\\item ");
        for (TexList el : list) {
            el.toTex(builder);
        }
    }
}
