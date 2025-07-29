package com.denizenscript.denizen.scripts.containers.core;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizen.utilities.FormattedTextHelper;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.YamlConfiguration;
import com.denizenscript.denizen.utilities.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class BookScriptContainer extends ScriptContainer {

    // <--[language]
    // @name Book Script Containers
    // @group Script Container System
    // @description
    // Book script containers are similar to item script containers, except they are specifically
    // for the book items. They work with the ItemTag object, and can be fetched
    // with the Object Fetcher by using the ItemTag constructor book_script_name
    // Example: - give <player> my_book
    //
    // <code>
    // Book_Script_Name:
    //
    //     type: book
    //
    //     # The 'custom name' can be anything you wish.
    //     # | All book scripts MUST have this key!
    //     title: custom name
    //
    //     # The 'custom name' can be anything you wish.
    //     # | All book scripts MUST have this key!
    //     author: custom name
    //
    //     # Defaults to true. Set to false to spawn a 'book and quill' instead of a 'written book'.
    //     # | Some book scripts might have this key!
    //     signed: true/false
    //
    //     # Each -line in the text section represents an entire page.
    //     # To create a newline, use the tag <n>. To create a paragraph, use <p>.
    //     # | All book scripts MUST have this key!
    //     text:
    //     - page
    //     - ...
    // </code>
    //
    // -->
    public BookScriptContainer(YamlConfiguration configurationSection, String scriptContainerName) {
        super(configurationSection, scriptContainerName);
        canRunScripts = false;
    }

    public ItemTag getBookFrom(TagContext context) {
        Item material = Items.WRITTEN_BOOK;
        if (contains("signed", String.class) && getString("signed").equalsIgnoreCase("false")) {
            material = Items.WRITABLE_BOOK;
        }
        ItemTag stack = new ItemTag(material);
        return writeBookTo(stack, context);
    }

    public ItemTag writeBookTo(ItemTag book, TagContext context) {
        ItemStack target;
        if (context == null) {
            context = new BukkitTagContext(null, /*null, */new ScriptTag(this));
        }
        if (contains("signed", String.class)) {
            target = getString("signed").equalsIgnoreCase("false") ? new ItemStack(Items.WRITABLE_BOOK) : new ItemStack(Items.WRITTEN_BOOK);

            if (!book.getItemStack().is(target.getItem())) {
                CompoundTag compoundtag = book.getItemStack().getTag();
                if (compoundtag != null) {
                    target.setTag(compoundtag.copy());
                }
            }
        }
        else
        {
            target = book.getItemStack();
        }

        if (contains("title", String.class)) {
            String title = getString("title");
            title = TagManager.tag(title, context);
            target.addTagElement("title", StringTag.valueOf(title));

        }
        if (contains("author", String.class)) {
            String author = getString("author");
            author = TagManager.tag(author, context);
            target.addTagElement("title", StringTag.valueOf(author));
        }
        if (contains("text", List.class)) {
            ListTag listtag = new ListTag();
            List<String> pages = getStringList("text");
            for (String page : pages) {
                page = TagManager.tag(page, context);
                listtag.add(StringTag.valueOf(page));
                target.addTagElement("pages", listtag);
            }
        }
        book.setItemStack(target);
        return book;
    }
}
