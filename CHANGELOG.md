# 0.9

- Updated to Minecraft 1.21.11
- Rewritten the config system:
     - YACL is now an optional dependency
     - Only non-default config values are saved now
     - Allows for better config updating in the future (no more resetting the config every update!)
     - All previous config values will be reset, however, the config file from older versions will remain untouched 
     - Some default config values have been fixed
- Added toggles to disable all block placing or breaking particles
- Lava bubble pop particles now match the lava height more closely
- Adjusted Redstone Dust and Redstone Interaction Dust particles to look closer to vanilla redstone particles
- A bunch of internal refactors have been made this update, do make sure to report any issues you find! 