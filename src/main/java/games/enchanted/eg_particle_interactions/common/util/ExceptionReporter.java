package games.enchanted.eg_particle_interactions.common.util;

import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExceptionReporter {
    private final String name;
    private final Logger logger;

    private final Queue<ExceptionEntry> exceptionEntries = new ConcurrentLinkedQueue<>();

    public ExceptionReporter(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(Constants.MOD_NAME + "/" + name);
    }

    public void consumeException(Identifier resourceId, Exception exception) {
        this.exceptionEntries.add(new ExceptionEntry(resourceId, exception));
    }

    public void logExceptions() {
        if(this.exceptionEntries.isEmpty()) return;

        Iterator<ExceptionEntry> it = this.exceptionEntries.iterator();

        this.logger.error("-- One or more {} failed to load! Listing all reasons below:", name);

        while (it.hasNext()) {
            var next = it.next();
            this.logger.error("Failed to load '{}', reason: \n{}", next.resourceId, next.exception.getMessage());
        }

        this.logger.info("-- Finished listing invalid {}", this.name);

        this.clear();
    }

    public void clear() {
        this.exceptionEntries.clear();
    }

    public record ExceptionEntry(Identifier resourceId, Exception exception) {
    }
}
