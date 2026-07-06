package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ParticleOverrides extends SimplePreparableReloadListener<ParticleOverrides.Preparation> {
    public static final Identifier VANILLA_OVERRIDE_ID = ParticleInteractionsMod.id("vanilla");
    public static final Identifier EMPTY_OVERRIDE_ID = ParticleInteractionsMod.id("empty");
    public static final Identifier FALLBACK_OVERRIDE_ID = ParticleInteractionsMod.id("internal/fallback");

    private final BiMap<Identifier, ParticleOverride> overrideById = HashBiMap.create();
    private final FileToIdConverter fileToIdConverter = FileToIdConverter.json(Constants.MOD_ID + "/particle_overrides");
    private final ExceptionReporter exceptionReporter = new ExceptionReporter("Particle Overrides");

    public static final ParticleOverrides INSTANCE = new ParticleOverrides();
    
    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleOverride> overrideList = new HashMap<>();

        for (Map.Entry<Identifier, Resource> overrideResource : this.fileToIdConverter.listMatchingResources(manager).entrySet()) {
            Identifier fileId = overrideResource.getKey();
            this.parseOverride(fileId, overrideResource.getValue(), overrideList);
        }

        return new Preparation(overrideList);
    }

    protected void parseOverride(Identifier fileId, Resource resource, Map<Identifier, ParticleOverride> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(this.fileToIdConverter.fileToId(fileId), ParticleOverride.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (Exception e) {
            this.exceptionReporter.consumeException(fileId, e);
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        clearRegisteredOverrides();
        registerOverride(FALLBACK_OVERRIDE_ID, ParticleOverride.EMPTY);

        Map<Identifier, ParticleOverride> preparedOverrides = preparations.overrideList();
        for (Map.Entry<Identifier, ParticleOverride> overrideEntry : preparedOverrides.entrySet()) {
            if(overrideEntry.getKey().equals(FALLBACK_OVERRIDE_ID)) continue;
            registerOverride(overrideEntry.getKey(), overrideEntry.getValue());
        }

        this.exceptionReporter.logExceptions();
    }

    private void clearRegisteredOverrides() {
        this.overrideById.clear();
    }


    void registerOverride(Identifier id, ParticleOverride override) {
        this.overrideById.put(id, override);
    }

    public ParticleOverride getOverrideOrFallback(Identifier id) {
        ParticleOverride override = this.overrideById.get(id);
        if(override == null) {
            if(!this.overrideById.containsKey(FALLBACK_OVERRIDE_ID)) {
                throw new IllegalStateException("Fallback particle override does not exist. that should not happen brh");
            }
            return this.overrideById.get(FALLBACK_OVERRIDE_ID);
        }
        return override;
    }

    public Identifier getIdOrThrow(ParticleOverride override) {
        Identifier id = this.overrideById.inverse().get(override);
        if(id == null) {
            throw new IllegalStateException("Tried to get id for unregistered particle override '" + override + "'");
        }
        return id;
    }


    public static ParticleOverride overrideOrFallback(Identifier id) {
        return INSTANCE.getOverrideOrFallback(id);
    }

    public static Identifier idOrThrow(ParticleOverride override) {
        return INSTANCE.getIdOrThrow(override);
    }

    protected record Preparation(Map<Identifier, ParticleOverride> overrideList) {
    }
}
