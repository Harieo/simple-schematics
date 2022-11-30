package net.harieo.schematics.paper;

import net.harieo.schematics.paper.config.SchematicToolConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SchematicsPlugin extends JavaPlugin {

    private SchematicToolConfiguration schematicToolConfiguration;

    @Override
    public void onEnable() {
        this.schematicToolConfiguration = new SchematicToolConfiguration(this);
    }

    public SchematicToolConfiguration getSchematicToolConfiguration() {
        return schematicToolConfiguration;
    }

}
