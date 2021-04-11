package markup;

import java.util.List;

public class UnorderedList extends AbstractList {
    public UnorderedList(List<ListItem> list) {
        super(list);
    }

    @Override
    protected String getEnvironment() {
        return "itemize";
    }
}