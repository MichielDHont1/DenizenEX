package com.denizenscript.denizencore.scripts.commands.queue;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.QueueTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.data.ActionableDataProvider;
import com.denizenscript.denizencore.utilities.data.DataAction;
import com.denizenscript.denizencore.utilities.data.DataActionHelper;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;

public class DefineCommand extends AbstractCommand {

    public DefineCommand() {
        setName("define");
        setSyntax("define [<id>](:<action>)[:<value>]");
        setRequiredArguments(1, 2);
        isProcedural = true;
        allowedDynamicPrefixes = true;
    }

    // <--[command]
    // @Name Define
    // @Syntax define [<id>](:<action>)[:<value>]
    // @Required 1
    // @Maximum 2
    // @Short Creates a temporary variable inside a script queue.
    // @Synonyms Definition
    // @Group queue
    // @Guide https://guide.denizenscript.com/guides/basics/definitions.html
    //
    // @Description
    // Definitions are queue-level 'variables' that can be used throughout a script, once defined, by using the <[<id>]> tag.
    // Definitions are only valid on the current queue and are not transferred to any new queues constructed within the script,
    // such as by a 'run' command, without explicitly specifying to do so.
    //
    // Definitions are lighter and faster than creating a temporary flag.
    // Definitions are also automatically removed when the queue is completed, so there is no worry for leaving unused data hanging around.
    //
    // This command supports data actions, see <@link language data actions>.
    //
    // Definitions can be sub-mapped with the '.' character, meaning a def named 'x.y.z' is actually a def 'x' as a MapTag with key 'y' as a MapTag with key 'z' as the final defined value.
    // In other words, "<[a.b.c]>" is equivalent to "<[a].get[b].get[c]>"
    //
    // @Tags
    // <[<id>]> to get the value assigned to an ID
    // <QueueTag.definition[<definition>]>
    // <QueueTag.definitions>
    //
    // @Usage
    // Use to make complex tags look less complex, and scripts more readable.
    // - narrate "You invoke your power of notice..."
    // - define range <player.flag[range_level].mul[3]>
    // - define blocks <player.flag[noticeable_blocks]>
    // - define count <player.location.find_blocks[<[blocks]>].within[<[range]>].size>
    // - narrate "<&[base]>[NOTICE] You have noticed <[count].custom_color[emphasis]> blocks in the area that may be of interest."
    //
    // @Usage
    // Use to validate a player input to a command script, and then output the found player's name.
    // - define target <server.match_player[<context.args.get[1]>]||null>
    // - if <[target]> == null:
    //   - narrate "<red>Unknown player target."
    //   - stop
    // - narrate "You targeted <[target].name>!"
    //
    // @Usage
    // Use to keep the value of a tag that you might use many times within a single script.
    // - define arg1 <context.args.get[1]>
    // - if <[arg1]> == hello:
    //     - narrate Hello!
    // - else if <[arg1]> == goodbye:
    //     - narrate Goodbye!
    //
    // @Usage
    // Use to remove a definition.
    // - define myDef:!
    //
    // @Usage
    // Use to make a MapTag definition and set the value of a key inside.
    // - define myroot.mykey MyValue
    // - define myroot.myotherkey MyOtherValue
    // - narrate "The main value is <[myroot.mykey]>, and the map's available key set is <[myroot].keys>"
    //
    // -->

    public static class DefinitionActionProvider extends ActionableDataProvider {

        public ScriptQueue queue;

        @Override
        public ObjectTag getValueAt(String keyName) {
            return queue.getDefinitionObject(keyName);
        }

        @Override
        public void setValueAt(String keyName, ObjectTag value) {
            queue.addDefinition(keyName, value);
        }
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("definition")) {
                if (CoreUtilities.contains(arg.getRawValue(), ':')) {
                    DefinitionActionProvider provider = new DefinitionActionProvider();
                    provider.queue = scriptEntry.getResidingQueue();
                    scriptEntry.addObject("action", DataActionHelper.parse(provider, arg, scriptEntry.context));
                }
                else {
                    scriptEntry.addObject("definition", new ElementTag(CoreUtilities.toLowerCase(arg.getValue())));
                }
            }
            else if (!scriptEntry.hasObject("value")) {
                scriptEntry.addObject("value", arg.hasPrefix() && arg.object instanceof ElementTag ? arg.getRawElement() : arg.object);
            }
            else {
                arg.reportUnhandled();
            }
        }
        if ((!scriptEntry.hasObject("definition") || !scriptEntry.hasObject("value")) && !scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a definition and value!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag definition = scriptEntry.getElement("definition");
        ObjectTag value = scriptEntry.getObjectTag("value");
        ElementTag remove = scriptEntry.getElement("remove");
        DataAction action = (DataAction) scriptEntry.getObject("action");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), new QueueTag(scriptEntry.getResidingQueue()), definition, value, action, remove);
        }
        if (action != null) {
            action.execute(scriptEntry.getContext());
            return;
        }
        scriptEntry.getResidingQueue().addDefinition(definition.asString(), value.duplicate());
    }
}
