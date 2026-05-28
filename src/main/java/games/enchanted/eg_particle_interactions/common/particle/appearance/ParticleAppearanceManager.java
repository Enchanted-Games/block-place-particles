package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ParticleAppearanceManager extends SimplePreparableReloadListener<ParticleAppearanceManager.Prepare> {
    public static final Codec<ParticleAppearance.Reference> ID_CODEC = ModCodecs.IDENTIFIER.xmap(
        ParticleAppearance.Reference::new,
        ParticleAppearance.Reference::id
    );

    @Nullable
    private static Codec<ParticleAppearance.Reference> REFERENCE_CODEC;

    private static final Map<Identifier, ParticleAppearance> SOURCE_BY_ID = new HashMap<>();
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/appearances");

    public static final ParticleAppearanceManager INSTANCE = new ParticleAppearanceManager();

    @Override
    protected Prepare prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleAppearance> textureSourceMap = new HashMap<>();

        for (Map.Entry<Identifier, Resource> overrideResource : FILE_TO_ID_CONVERTER.listMatchingResources(manager).entrySet()) {
            Identifier fileId = overrideResource.getKey();
            parseSource(fileId, overrideResource.getValue(), textureSourceMap);
        }

        return new Prepare(textureSourceMap);
    }

    protected static void parseSource(Identifier fileId, Resource resource, Map<Identifier, ParticleAppearance> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(FILE_TO_ID_CONVERTER.fileToId(fileId), ParticleAppearance.codec().parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (Exception e) {
            Logging.error("Failed to parse particle appearance '{}'", fileId, e);
        }
    }

    @Override
    protected void apply(Prepare preparations, ResourceManager manager, ProfilerFiller profiler) {
        SOURCE_BY_ID.clear();
        SOURCE_BY_ID.putAll(preparations.textureSourceMap());
    }

    public static ParticleAppearance get(Identifier sourceId) {
        if(!(SOURCE_BY_ID.containsKey(sourceId))) {
            return ParticleAppearance.MISSING_APPEARANCE.get();
        }
        return SOURCE_BY_ID.get(sourceId);
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
