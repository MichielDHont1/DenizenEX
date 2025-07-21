package com.denizenscript.denizen.utilities.command;

import com.denizenscript.denizen.Denizen;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandEvents {

    public CommandEvents() {
        //todo register event
    }

    @SubscribeEvent//todo prio low
    public void onPlayerCommandSend(CommandEvent event) {
//        event.getCommands().remove("denizenclickable");
//        event.getCommands().remove("denizen:denizenclickable");
    }
}
