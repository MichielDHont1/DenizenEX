package com.denizenscript.denizencore.scripts.commands.queue;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class ElseCommand extends AbstractCommand {

    public ElseCommand() {
        setName("else");
        setSyntax("else (if <comparison logic>)");
        setRequiredArguments(0, -1);
        isProcedural = true;
    }

    // <--[command]
    // @Name Else
    // @Syntax else (if <comparison logic>)
    // @Required 0
    // @Maximum -1
    // @Short Helper command for usage with the if command.
    // @Group queue
    // @Guide https://guide.denizenscript.com/guides/basics/if-command.html
    //
    // @Description
    // A helper command to use with if commands.
    // See <@link command if> command documentation.
    //
    // @Tags
    // See IF command documentation.
    //
    // @Usage
    // See IF command documentation.
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        // If this command executes normally, it's misplaced. It should always be skipped past under normal execution.
        Debug.echoError(scriptEntry, "Misplaced ELSE command.");
    }
}
