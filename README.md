# Dungeon Devices - Fabric 1.21.11 port (PHASE 1)

Original mod (Forge 1.18.2, v1.3.23) by Faa and Zaky. Ported with the authors' permission.
Port: Claude (Anthropic). Class names were de-obfuscated (`z` -> `FurnitureBlock`, `ab` -> `SeatBlockEntity`, ...).

## Build
1. Generate a blank 1.21.11 template at https://fabricmc.net/develop/ and copy its exact
   `loader_version`, `loom_version`, `fabric_version`, `yarn_mappings` into `gradle.properties`
   (the values here are placeholders). Copy its `gradle/` wrapper + `gradlew` files in too.
2. `./gradlew build`  -> paste any compile errors back to me; this code has NOT been compiled yet.

## Done (phase 1)
- Project scaffold, `fabric.mod.json`, registries (blocks, block items, block entity, 10 seat entities, 2 creative tabs)
- All 10 furniture blocks with original hitboxes (generated from the original data) + multi-block placement
- Seating logic: SeatBlockEntity / SeatEntity (occupant, flip, release, redstone + comparator output)
- All building-block sets, doors, trapdoors, handle/plank/brick items
- Resources migrated to 1.21 formats: recipes, loot tables, advancements, tags, item model definitions
  (`minecraft:chain` -> `iron_chain`; netherite whip smithing now needs the vanilla template)

## Not done yet (phase 2+)
- Whips, paddle, cane, crop, branding iron (+ `forge:separate_transforms` item models -> `select` item definitions)
- Shackles / chastity items (GeckoLib 3 armor -> GeckoLib 5, new equipment asset format), effect-toggle capability -> Fabric attachment
- Enchantments (5, now datapack-driven), mixins (MixinEntity, MixinFox), sounds events, config
- Networking + client: Player Animation Library poses, seat renderer, vibration sound, Supplementaries compat

## Behaviour notes
- Original bug fixed: block entity never restored the seat entity id (checked the wrong NBT type).
- Original quirk kept: WEIGHTED_WOODEN_HORSE and WALL_CUFF are not in the block entity's valid-block list.
- Forge-only `shouldRiderSit` has no equivalent; riding pose is handled in phase 2.
