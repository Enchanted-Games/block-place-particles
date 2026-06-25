# 0.10.0-alpha.3

This version comes with an absolute ton of internal changes, I don't expect anything to be majorly broken but a couple features haven't been extensively tested yet. As always please be sure to report any bugs you find!

## Changes in alpha 3
- Rewritten the particle palette system to be usable for fluids as well as blocks
- Particle palettes can now be defined per block/fluid state instead of just per block id
- Added 'texture debug logs' option back to the debug section in the config screen
- Added a new block/sprinted_on particle origin for block sprinting particles
- Added a new block/mace_smash particle origin for mace smash particles
- Added a position_offset field to particle emitters

## Bug fixes
- Fixed mace smash particles not flying up into the air as far as they do in vanilla
- Fixed some entity landing particles falling through the block
- Hay bales no longer emit red particles sometimes