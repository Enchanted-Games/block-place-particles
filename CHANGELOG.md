# 0.10.2

This version comes with some more minor bug fixes, tweaks to existing particles, and some new resourcepack features

## Changes & Fixes
- Ambient leaf particles now use the particle override specified for that leaf block
    - Spruce leaves now have falling needles, and azalea leaves now have appropriate falling leaf particles
- Honey block particles are no longer bouncy
- Fixed block placement particles sometimes clipping into the block directly above or below
- Fixed bouncy particles taking an extra tick to bounce instead of immediately after colliding something
- Particle collision and interaction with fluids should now be much more accurate

## Resourcepack changes
- Added a new particle origin: `block/ambient_leaves`. This only works for leaf blocks that already emit particles.
- Resourcepacks can now define overlays that only apply to certain versions of Particle Interactions
    - Documentation will be available for this on the docs website
- Added a new `physics/bounciness_decay` component. This works similarly to other 'X decay' components in that the value is multiplied by the bounciness every time the particle bounces
- The `lifetime_events` component has been renamed to `events`
    - All event actions can now be specified in either the `events` component or `events` field of particle appearances
    - Spark particles make use of this to avoid duplicating the particle definition for soul and regular sparks
- Added a `facing_camera_mode` field to particle appearances which controls particle billboarding
- Trying to use an invalid particle override now logs a warning instead of refusing to load the file
- Fixed the `flow_acceleration` field in `physics/wind_config` component not respecting negative values
- The documentation site is now complete: https://particle-interactions.enchanted.games/rp/introduction.html