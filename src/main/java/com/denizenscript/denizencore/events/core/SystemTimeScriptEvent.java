package com.denizenscript.denizencore.events.core;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;

import java.util.Calendar;

public class SystemTimeScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // system time <'HH:MM'>
    // system time hourly|minutely|secondly
    //
    // @Switch every:<count> to only run the event every *count* times (like "on system time secondly every:5" for every 5 seconds).
    //
    // @Synonyms cron
    //
    // @Group Core
    //
    // @Triggers when the system time changes to the specified value.
    // The system time is the real world time set in the server's operating system.
    // It is not necessarily in sync with the game server time, which may vary (for example, when the server is lagging).
    // For events based on in-game time passage, use <@link event delta time> or <@link command wait>.
    //
    // @Context
    // <context.hour> returns the exact hour of the system time.
    // <context.minute> returns the exact minute of the system time.
    //
    // @Example
    // on system time hourly:
    // - announce "Whoa an hour passed!"
    // @Example
    // on system time 12:00:
    // - announce "Whoa it's noon!"
    // @Example
    // on system time 03:00:
    // - announce "Daily restart in 5 minutes!"
    // - wait 5m
    // - adjust server restart
    //
    // -->

    public static SystemTimeScriptEvent instance;

    public SystemTimeScriptEvent() {
        instance = this;
        registerCouldMatcher("system time <'HH:MM'>");
        registerCouldMatcher("system time hourly|minutely|secondly");
        registerSwitches("every");
    }

    public ElementTag hour;

    public ElementTag minute;

    public long seconds;

    @Override
    public boolean matches(ScriptPath path) {
        String time = path.rawEventArgAt(2);
        String countString = path.switches.get("every");
        int count = countString == null ? 1 : Integer.parseInt(countString);
        if (time.equals("secondly")) {
            if (seconds % count != 0) {
                return false;
            }
        }
        else if (time.equals("minutely")) {
            if (!minuteChanged) {
                return false;
            }
            long minutes = seconds / 60;
            if (minutes % count != 0) {
                return false;
            }
        }
        else if (time.equals("hourly")) {
            if (!minuteChanged || lM != 0) {
                return false;
            }
            long hours = seconds / 3600;
            if (hours % count != 0) {
                return false;
            }
        }
        else if (!minuteChanged || !time.equals(hour.asString() + ":" + minute.asString())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "hour": return hour;
            case "minute": return minute;
        }
        return super.getContext(name);
    }

    boolean enab = false;

    @Override
    public void init() {
        enab = true;
    }

    @Override
    public void destroy() {
        enab = false;
    }

    int lH = 0;
    int lM = 0;
    long lS = 0;
    boolean minuteChanged = true;

    public void checkTime() {
        if (!enab) {
            return;
        }
        seconds = System.currentTimeMillis() / 1000;
        if (lS == seconds) {
            return;
        }
        lS = seconds;
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        minuteChanged = lH != h || lM != m;
        lH = h;
        lM = m;
        if (h < 10) {
            hour = new ElementTag("0" + h);
        }
        else {
            hour = new ElementTag(h);
        }
        if (m < 10) {
            minute = new ElementTag("0" + m);
        }
        else {
            minute = new ElementTag(m);
        }
        fire();
    }
}
