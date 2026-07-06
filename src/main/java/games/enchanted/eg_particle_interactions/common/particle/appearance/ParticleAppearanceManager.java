package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleAppearanceManager extends SimplePreparableReloadListener<ParticleAppearanceManager.Prepare> {
    public static final Codec<ParticleAppearance.Reference> ID_CODEC = ModCodecs.IDENTIFIER.xmap(
        ParticleAppearance.Reference::new,
        ParticleAppearance.Reference::id
    );

    @Nullable
    private static Codec<ParticleAppearance.Reference> REFERENCE_CODEC;

    private final Map<Identifier, ParticleAppearance> sourceById = new HashMap<>();
    private final FileToIdConverter fileToIdConverter = FileToIdConverter.json(Constants.MOD_ID + "/appearances");
    private final List<Identifier> missingLogged = new ArrayList<>();
    private final ExceptionReporter exceptionReporter = new ExceptionReporter("Particle Appearances");

    public static final ParticleAppearanceManager INSTANCE = new ParticleAppearanceManager();

    @Override
    protected Prepare prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleAppearance> textureSourceMap = new HashMap<>();

        for (Map.Entry<Identifier, Resource> overrideResource : this.fileToIdConverter.listMatchingResources(manager).entrySet()) {
            Identifier fileId = overrideResource.getKey();
            this.parseAppearance(fileId, overrideResource.getValue(), textureSourceMap);
        }

        return new Prepare(textureSourceMap);
    }

    protected void parseAppearance(Identifier fileId, Resource resource, Map<Identifier, ParticleAppearance> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(this.fileToIdConverter.fileToId(fileId), ParticleAppearance.codec().parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (Exception e) {
            this.exceptionReporter.consumeException(fileId, e);
        }
    }

    @Override
    protected void apply(Prepare preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.sourceById.clear();
        this.sourceById.putAll(preparations.textureSourceMap());
        this.missingLogged.clear();

        this.exceptionReporter.logExceptions();
    }

    public ParticleAppearance getById(Identifier sourceId) {
        if(!(this.sourceById.containsKey(sourceId))) {
            if(!this.missingLogged.contains(sourceId)) {
                this.missingLogged.add(sourceId);
                Logging.warn("Unknown particle appearance '" + sourceId + "'");
            }
            return ParticleAppearance.MISSING_APPEARANCE.get();
        }
        return this.sourceById.get(sourceId);
    }

    public static ParticleAppearance get(Identifier id) {
        return INSTANCE.getById(id);
    }

    public static Codec<ParticleAppearance.Reference> referenceCodec() {
        if(REFERENCE_CODEC == null) {
            REFERENCE_CODEC = ID_CODEC.withAlternative(
                ParticleAppearance.codec().xmap(
                    ParticleAppearance.InlineRef::new,
                    ParticleAppearance.InlineRef::lookupObject
                )
            );
        }
        return REFERENCE_CODEC;
    }

    protected record Prepare(Map<Identifier, ParticleAppearance> textureSourceMap) {
    }
}
