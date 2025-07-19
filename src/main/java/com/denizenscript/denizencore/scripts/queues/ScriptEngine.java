package com.denizenscript.denizencore.scripts.queues;

import com.denizenscript.denizencore.scripts.commands.CommandExecutor;
import com.denizenscript.denizencore.scripts.queues.core.TimedQueue;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.scripts.ScriptEntry;

public class ScriptEngine {

    static boolean shouldHold(ScriptQueue scriptQueue) {
        if (scriptQueue instanceof TimedQueue && ((TimedQueue) scriptQueue).isPaused()) {
            return true;
        }
        ScriptEntry last = scriptQueue.getLastEntryExecuted();
        if (last == null || !last.shouldWaitFor()) {
            return false;
        }
        if (!(scriptQueue instanceof TimedQueue)) {
            scriptQueue.forceToTimed(null);
        }
        return true;
    }

    public static void revolveOnceForce(ScriptQueue scriptQueue) {
        ScriptEntry scriptEntry = scriptQueue.getNext();
        if (scriptEntry == null) {
            return;
        }
        scriptEntry.setSendingQueue(scriptQueue);
        scriptEntry.updateContext();
        scriptQueue.setLastEntryExecuted(scriptEntry);
        if (scriptEntry.internal.waitfor) {
            scriptQueue.holdingOn = scriptEntry;
        }
        try {
            CommandExecutor.execute(scriptEntry);
        }
        catch (Throwable e) {
            Debug.echoError(scriptEntry, "An exception has been called with this command (while revolving the queue forcefully)!");
            Debug.echoError(scriptEntry, e);
        }
    }

    public static void revolve(ScriptQueue scriptQueue) {
        if (shouldHold(scriptQueue)) {
            return;
        }
        ScriptEntry scriptEntry = scriptQueue.getNext();
        while (scriptEntry != null) {
            scriptEntry.setSendingQueue(scriptQueue);
            scriptEntry.updateContext();
            scriptQueue.setLastEntryExecuted(scriptEntry);
            if (scriptEntry.internal.waitfor) {
                scriptQueue.holdingOn = scriptEntry;
            }
            CommandExecutor.execute(scriptEntry);
            if (scriptQueue instanceof TimedQueue) {
                TimedQueue delayedQueue = (TimedQueue) scriptQueue;
                if (delayedQueue.isDelayed() || delayedQueue.isPaused()) {
                    break;
                }
                if (delayedQueue.isInstantSpeed() || scriptEntry.isInstant()) {
                    if (shouldHold(scriptQueue)) {
                        return;
                    }
                    scriptEntry = scriptQueue.getNext();
                }
                else {
                    break;
                }
            }
            else if (scriptEntry.isInstant()) {
                if (shouldHold(scriptQueue)) {
                    return;
                }
                scriptEntry = scriptQueue.getNext();
            }
            else {
                break;
            }
        }
    }
}
