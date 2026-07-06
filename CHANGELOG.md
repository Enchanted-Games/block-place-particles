# 0.10.1

This version comes with some fixes to bugs that were introduced in v0.10 as well as some smaller new features!

## New features
- Water splash particles are now biome tinted when an entity enters water
- Mace smash particles now use the block particle of the block below the particle, instead of always using the block below the entity that was hit
- Added new particles for slime blocks, these look similar to the vanilla particles but are bouncy
- Added new particles for honey blocks, these look similar to the vanilla particles but are a bit more slippery and floaty

## Changes
- Removed the particle render distance sliders from the config screen. The options are still there however they will be fully removed in a future release as they never really worked correctly
- Z-fighting fix now always applies to particle interactions' particles, the toggle only works on vanilla and most other mod particles now
- Split the 'General / Compatibility' config group into separate groups

## Resourcepack changes
- Added a new particle component `physics/intangible_layers` which controls whether the particle can collide with terrain or fluids
- Friction no longer applies to the y velocity value, only x and z.
- The `fluid_splash` particle override has been split into `water_fluid_splash` and `lava_fluid_splash`, this also applies to the respective block lists
- There are now notifications ingame if some resourcepack feature have failed to load
- Resourcepack errors are now logged to the output log in a clearer and more organised way
- **Proper documentation is still being worked on for resourcepack features!** However feel free to poke around the mod files if you're curious how it all works

## Fixes
- Fix some particles sometimes getting stuck underwater
- Fix non-buoyant particles sometimes sticking to a fluid surface
- Added translations for debugging config options