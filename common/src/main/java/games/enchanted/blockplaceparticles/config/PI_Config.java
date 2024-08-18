package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class PI_Config {
    public static final Path CONFIG_PATH = Services.PLATFORM.getConfigPath().resolve(ParticleInteractionsMod.MOD_ID);

    public static final ConfigClassHandler<PI_Config> HANDLER = ConfigClassHandler.createBuilder(PI_Config.class)
        .id(ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "config"))

        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(CONFIG_PATH)
            //.setJson5(true) // Uncomment this line to use JSON5 instead of JSON.
            .build())
        .build();

    public static void load() {
        HANDLER.load();
    }

    public static void save() {
        HANDLER.save();
    }

    public static PI_Config instance() {
        return HANDLER.instance();
    }


    // BLOCKS CONFIGS

    // FLUIDS CONFIG

}
