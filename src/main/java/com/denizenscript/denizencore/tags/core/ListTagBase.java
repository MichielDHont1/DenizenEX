package com.denizenscript.denizencore.tags.core;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagManager;

public class ListTagBase {

    public ListTagBase() {

        // <--[tag]
        // @attribute <list[(<list>)]>
        // @returns ListTag
        // @description
        // Returns a list object constructed from the input value.
        // Give no input to create an empty list.
        // Refer to <@link ObjectType ListTag>.
        // -->
        TagManager.registerStaticTagBaseHandler(ListTag.class, "list", (attribute) -> {
            if (!attribute.hasParam()) {
                return new ListTag();
            }
            return ListTag.getListFor(attribute.getParamObject(), attribute.context);
        });
    }
}
