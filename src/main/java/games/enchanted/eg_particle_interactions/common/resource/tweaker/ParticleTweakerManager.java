package games.enchanted.eg_particle_interactions.common.resource.tweaker;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleTweakerManager extends SimplePreparableReloadListener<ParticleTweakerManager.Prepare> {
    public static final Codec<ParticleTweaker.Reference> ID_CODEC = ModCodecs.IDENTIFIER.xmap(
        ParticleTweaker.Reference::new,
        ParticleTweaker.Reference::id
    );

    private final Map<Identifier, ParticleTweaker> tweakerById = new HashMap<>();
    private final FileToIdConverter fileToIdConverter = FileToIdConverter.json(Constants.MOD_ID + "/particle_tweakers");
    private final List<Identifier> missingLogged = new ArrayList<>();
    private final ExceptionReporter exceptionReporter = new ExceptionReporter("Particle Tweakers");

    public static final ParticleTweakerManager INSTANCE = new ParticleTweakerManager();

    @Override
    protected Prepare prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleTweaker> preparedTweakersMap = new HashMap<>();

        for (Map.Entry<Identifier, Resource> resource : this.fileToIdConverter.listMatchingResources(manager).entrySet()) {
            Identifier fileId = resource.getKey();
            this.parseTweaker(fileId, resource.getValue(), preparedTweakersMap);
        }

        return new Prepare(preparedTweakersMap);
    }

    protected void parseTweaker(Identifier fileId, Resource resource, Map<Identifier, ParticleTweaker> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(this.fileToIdConverter.fileToId(fileId), ParticleTweaker.codec().parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (Exception e) {
            this.exceptionReporter.consumeException(fileId, e);
        }
    }

    @Override
    protected void apply(Prepare preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.tweakerById.clear();
        this.tweakerById.putAll(preparations.preparedTweakers());
        this.missingLogged.clear();

        this.exceptionReporter.logExceptions();
    }

    public ParticleTweaker getById(Identifier sourceId) {
        if(!(this.tweakerById.containsKey(sourceId))) {
            if(!this.missingLogged.contains(sourceId)) {
                this.missingLogged.add(sourceId);
                Logging.warn("Unknown particle appearance '" + sourceId + "'");
            }
            return new ParticleTweaker(false);
        }
        return this.tweakerById.get(sourceId);
    }

    public static ParticleTweaker get(Identifier id) {
        return INSTANCE.getById(id);
    }

    protected record Prepare(Map<Identifier, ParticleTweaker> preparedTweakers) {
    }
}
