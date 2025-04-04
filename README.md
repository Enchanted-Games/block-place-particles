# Particle Interactions
A mod that adds particles to more interactions in the game, such as when placing a block or stripping a log. The mod aims to be as configurable as possible and doesn't need to be installed on the server!

**This mod is fully client side**, however, a few particles added by the mod won't spawn when other players use certain items, place certain blocks, etc. Better multiplayer support is planned in the future, which may require the mod to be installed server-side

Every single feature added by this mod can be adjusted or disabled entirely in the mods' config menu! To access this, click the Mods button on the title or pause screen (on Fabric you need [Mod Menu](https://modrinth.com/mod/modmenu)), click on the Particle Interactions mod, and click the config button

The mod is currently only being updated for the latest Minecraft version (1.21.4 as of writing). Mod updates will not be backported to older versions until at least the full 1.0 release (or until i have more time to do so)

## This mod is in beta!
Some things may be changed or removed entirely before the full release. The config may break between versions before the full release. If you spot bugs / crashes / other mod incompatibilities, you can report them on [the GitHub issue tracker](https://github.com/Enchanted-Games/block-place-particles/issues)!
Please test this with your favourite mods and report any issues or unexpected behaviour on the issue tracker!

## Current Features as of v0.7
### Particles
- Unique block breaking/placement particles which appear when breaking, placing or sprinting on certain blocks (Block Particle Overrides)

<details>
<summary>Full list of block particle overrides</summary>
<ul>
<li>Snowflakes (applies to snow blocks, snow layers, and powder snow by default)</li>
<li>Firefly (applies to firefly bushes by default)</li>
<li>Cherry petals (applies to cherry leaves, cherry saplings, and pink petals by default)</li>
<li>Azalea and flowering azalea leaves (applies to (flowering) azalea and (flowering) azalea leaves by default)</li>
<li>Pine Leaves (applies to every birch leaves and spruce leaves by default)</li>
<li>Generic Leaves (applies to every other vanilla leaf block by default)</li>
<li>Flower Petal (applies to cave vines, twisting vines, weeping vines, bushes, and most vanilla and modded flowers by default)</li>
<li>Grass Blade (applies to grass blocks, grass, tall grass, seagrass, vines, glow lichen, dead bushes, and most vanilla and modded crops by default)</li>
<li>Heavy Grass Blade (applies to hanging roots, pale hanging moss, cobwebs, and tripwire by default)</li>
<li>Moss Clump (applies to moss and moss carpets by default)</li>
<li>Pale Moss Clump (applies to pale moss and pale moss carpets by default, only in minecraft 1.21.2 and above)</li>
<li>Dust (applies to sand, gravel, sus sand, sus gravel, concrete powder, soul sand, and soul soil by default)</li>
<li>Redstone Dust (applies to redstone, redstone blocks, comparators, repeaters, and redstone torches by default)</li>
<li>Block Shatter (applies to nether portals by default)</li>
</ul>
</details>

- Bubbles are spawned when placing or breaking blocks underwater
- Sparks are spawned when using a flint and steel
- Fire and smoke particles are spawned when using a fire charge
- Sparks occasionally fly out of campfires and regular fires (sparks are blue for soul fire)
- Ember particles float out of campfires and regular fires (embers are blue for soul fire)
- Particles when tilling dirt, stripping logs, and flattening dirt
- Sparks fly from minecart wheels when they are travelling close to full speed on a rail (by default, this only happens when there is a block or entity inside the minecart)
- Sparks spawn from blazes randomly and when they are damaged
- Sparks spawn when combining or grinding items in anvils and stonecutters
- Dust particles spawn when brushing blocks that don't have a Block Particle Override specified, can be toggled in the config menu
- Red dust particles are spawned when interacting with redstone components (includes redstone, comparators, and repeaters)
- Falling blocks have particle effects when falling and when landing (uses the Block Particle Override for the particle)
- Random bubble streams can spawn underwater from seagrass
- Lava now has a bubble popping animation that can randomly appear
- Most flowers, foliage, and some other blocks such as cobwebs now spawn particles when you walk, run, or jump through them
- Furnaces and blast furnaces now emit ember and spark particles when smelting
- Smokers now emit large smoke particles when smelting
- Dust particles spawn when interacting with item frames
- Blocks which use the grass blade particle override will occasionally spawn fireflies when interacted with in swampy biomes at night

### Other Features
- Makes block breaking, block placing, eating, and similar particles pixel consistent
- Fixes particles z-fighting (glitching) with each other when looking straight down ([MC-91873](https://bugs.mojang.com/browse/MC-91873))
- A toggle to disable some extra physics calculations done by the mod to slightly improve performance

## Planned Features
You can view the full list of planned features [here!](https://github.com/Enchanted-Games/block-place-particles/issues/6)

## License
<p xmlns:cc="http://creativecommons.org/ns#" >Particle Interactions by <a rel="cc:attributionURL dct:creator" property="cc:attributionName" href="https://enchanted.games">ioblackshaw (a.k.a. Enchanted_Games)</a> is licensed under <a href="http://creativecommons.org/licenses/by-nc/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">CC BY-NC 4.0<img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1"><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1"><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nc.svg?ref=chooser-v1"></a></p> 
Video content creators may monetise videos including this work provided the license is followed.
