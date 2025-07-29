package com.denizenscript.denizen.utilities;

public class NamespacedKey {
    private final String namespace;
    private final String key;
    public NamespacedKey(String NameSpace, String Key)
    {
        namespace = NameSpace;
        key = Key;
    }

    public static NamespacedKey minecraft(String Key)
    {
        return new NamespacedKey("Minecraft", Key);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }
}
