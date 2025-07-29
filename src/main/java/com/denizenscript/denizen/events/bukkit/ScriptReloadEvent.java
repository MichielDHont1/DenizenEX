package com.denizenscript.denizen.events.bukkit;

import net.minecraftforge.eventbus.api.Event;

/**
 * denizen event bus event for when Denizen dScripts are reloaded. This fires after scripts are reloaded.
 * Also fires when scripts are loaded for the first time, despite the 're' part of the name.
 */
public class ScriptReloadEvent extends Event {
}
