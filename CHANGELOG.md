# 0.8

v0.8 comes with a couple new particle effects, some fixes and tweaks, and a new feature for resourcepacks

## New Particles / Changes

### Block Particle Overrides

- New Chain Links override (applies to vanilla chain and #c:chains block tag by default)
- New Sugar Cane override (applies to bamboo, mangrove roots, and sugar cane by default)
- Adjusted the texture of the Snowflake override to be more like the one seen in v0.4, now also behaves more like other 'dust' particles in the mod
- Dirt Path blocks now use the Grass Blade particle override

### Entity Particles

- New Lighting Strike particles, sparks and arcs will fly out from where a lightning strike hits for a short amount of time

### Item Interactions

- New Honey Collection particles, will spawn when harvesting honey bottles from a bee next or hive. Also replaces vanilla honey drip particles that appear underneath bee nests or hives

### Fluid Ambient

- Added a configurable list of blocks that can emit underwater bubble stream particles, it is no longer hardcoded to seagrass
- Underwater bubble stream particles no longer get stuck on the underside of blocks, instead they will pop when hitting the bottom of a block  

### General

- Adjusted the particle spawning logic for blocks with smaller hitbox sizes. Should result in better looking particles when placing / breaking small blocks such as buttons or snow layers
- Vanilla Firefly particles no longer get stuck on the top or bottom of blocks
- Added an option to disable fixes to Firefly particles. This includes fireflies dying in air (been in this mod since v0.7), and fireflies getting stuck on blocks
- Egg and snowball projectile particles now have outward velocity like they did around Minecraft 1.12

## Bugfixes

- Fixed the 'particle z-fighting fix' not working properly with some particle overrides
- Fixed lava bubble pop textures having a really high resolution

## Resourcepack Features

Added a way for resourcepacks to provide alternative particle palettes for blocks. These are located under `assets/<block namespace>/textures/eg_particle_interactions/particle_palettes/<block id>.png` and can be any size.
 The mod will attempt to use these palette textures for dynamically coloured particles, otherwise it will fallback to the block model's particle texture.

As a result of this, the colours for grass black particles on grass blocks are no longer hardcoded to the `grass_block_top` texture. Dirt paths, grass blocks, bamboo, and sugar cane have builtin palettes

## Other

- Switched to a stonecutter + arch loom build setup, shouldn't cause any issues but please report them if you spot any!