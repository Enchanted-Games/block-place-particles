# 0.10.0-alpha.1

This version comes with an absolute ton of internal changes, I don't expect anything to be majorly broken but a couple features haven't been extensively tested yet. As always please be sure to report any bugs you find!

## New resourcepack features
- Block and fluid particle overrides can now be controlled fully with resourcepacks
- Resourcepacks can now create new particle types which can be used in particle overrides or other places
- There are now 'emitter rule' files present in resourcepacks that can be used to control which particles are emitted for certain actions, for example when using a flint and steel
- **Proper documentation will come soon for resourcepack features!** However feel free to poke around the mod files if you're curious how it all works

## General changes
- Ported to Minecraft 26.2
- Old config files (from v0.9 only) are now updated automatically, you do not need to reset your config this time! yay!
- Falling blocks now emit particles based on the particle override when placed above non-solid blocks, this functionality is copied over from an older mod of mine (Better Falling Block Particles)
- Removed blaze damage particles, these may be added back in the future however they have been removed for now as I'm not happy with how they were implemented
- Changed the look of lava bucket placement particles to look more similar to lava surface bubble particles
- Reduced the duration of underwater bubble stream particles
- Removed some config options related to particle intensity as they were inconsistent with each other, this can now be controlled via resourcepacks instead (as mentioned above proper documentation coming soon)
- Sugar cane, mangrove root, and bamboo particles now float in water

## Fixes
- Sprinting on snowy grass blocks now shows snow particles instead of grass #60 #34
