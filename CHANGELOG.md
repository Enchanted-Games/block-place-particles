# 0.8.5

- Multiple improvements when spawning particles dynamically tinted based on the block texture
    - No longer reads pixels from the texture every single time a particle spawns
    - Fixed exceptions being thrown under certain circumstances while reloaded resources
- Reload listeners are now registered through Fabric Resource Loader (fabric only) for better mod compatibility