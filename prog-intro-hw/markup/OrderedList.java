package markup;

import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(List<ListItem> list) {
        super(list);
    }

    @Override
    protected String getEnvironment() {
        return "enumerate";
    }
}
