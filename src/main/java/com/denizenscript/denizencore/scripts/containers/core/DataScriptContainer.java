package com.denizenscript.denizencore.scripts.containers.core;

import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.Deprecations;
import com.denizenscript.denizencore.utilities.YamlConfiguration;

public class DataScriptContainer extends ScriptContainer {

    // <--[language]
    // @name Data Script Containers
    // @group Script Container System
    // @description
    // Data script containers are generic script containers for information that will be referenced by other scripts.
    //
    // No part of a 'data' script container is ever run as commands.
    //
    // There are no required keys.
    //
    // Generally, data is read using the <@link tag ScriptTag.data_key> tag.
    //
    // <code>
    // data_script_name:
    //
    //     type: data
    //
    //     # Your data here
    //     some_key: some value
    //     some_list_key:
    //     - some list value
    //     some_map_key:
    //         some_subkey: some value
    //
    // </code>
    //
    // -->

    public DataScriptContainer(YamlConfiguration configurationSection, String scriptContainerName) {
        super(configurationSection, scriptContainerName);
        canRunScripts = false;
        if (configurationSection.get("type").toString().equalsIgnoreCase("yaml data")) {
            Deprecations.yamlDataContainer.warn(this);
        }
    }
}
