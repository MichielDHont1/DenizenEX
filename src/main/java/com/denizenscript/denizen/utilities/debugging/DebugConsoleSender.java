package com.denizenscript.denizen.utilities.debugging;

//import com.denizenscript.denizen.utilities.PaperAPITools;
import com.denizenscript.denizencore.utilities.CoreConfiguration;


public class DebugConsoleSender {

    public static boolean showColor = true;


    public static void sendMessage(String string) {
        string = CoreConfiguration.debugPrefix + string.replace("<FORCE_ALIGN>", "                 ");
        System.out.println(string);
    }
}
