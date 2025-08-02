package com.denizenscript.denizen.nms.abstracts;

import com.denizenscript.denizen.objects.PlayerTag;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

public class OfflinePlayer {

    public GameProfile gameProfile;
    public UUID uuid;
    public String name;

    public OfflinePlayer(Player player)
    {
        uuid = player.getUUID();
        name = player.getName().getString();
        gameProfile = player.getGameProfile();
    }

    public OfflinePlayer(String name){
        Optional<GameProfile> optprofile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get("playerName");
        if (optprofile.isPresent())
        {
            uuid = optprofile.get().getId();
            name = optprofile.get().getName();
            gameProfile = optprofile.get();
        }
    }
    public OfflinePlayer(UUID testuuid){
        Optional<GameProfile> optprofile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(testuuid);
        if (optprofile.isPresent())
        {
            uuid = optprofile.get().getId();
            name = optprofile.get().getName();
            gameProfile = optprofile.get();
        }
    }
public String getName()
{
 return name;
}

public PlayerTag getUniqueId()
{
    return new PlayerTag(uuid);
}

public boolean hasPlayedBefore()
{
    return uuid != null;
}
public boolean isOnline()
{
    return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid) != null;
}

}
