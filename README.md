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

## Phase 2a (added)
- All 6 whips + paddle, cane, riding crop, branding iron (stats copied from the 1.18 tiers), sounds, creative tab icon
- 4 enchantments (Comforting Hit, Entanglement, Motivation, Punishment) as datapack JSON
- Cursed whip "Soul Pact", no-knockback + thorns-proof implements, release from furniture on dismount/logout/death
- Whip/implement item models converted from forge:separate-perspective to display_context item definitions

## Phase 2b-1 (added): restraint poses
- Player Animation Library dependency (successor of PlayerAnimator, which stops at 1.21.7)
- Server->client pose packet, re-sent on login / start-tracking; client plays/stops poses
- 9 pose files moved to assets/dungeon_devices/player_animations (legacy PlayerAnimator format is read natively)

## Phase 2b-2a (added): restraints, gameplay
- 8 restraint items (bra/belt/hand+ankle shackles, normal + cursed) with the original durability/armor/slots
- Ankle shackles slow the wearer (Extra Tight = Slowness III); hand shackles -1.5 reach (Extra Tight -0.2 more)
- Extra Tight enchantment (datapack), cursed belt vibrator sound (client->server->clients), "Zakyotsu" cat easter egg
- NOT yet drawn on the player: GeckoLib renderer is phase 2b-2b

## Not done yet (phase 2b-2b+)
- Shackles / chastity items (GeckoLib 3 armor -> GeckoLib 5, new equipment asset format), effect-toggle capability -> Fabric attachment
- GeckoLib renderer for the restraints (models/animation/bend), mixins MixinEntity/MixinFox (MixinEntity, MixinFox), sounds events, config
- Supplementaries compat, legacy id remap

## Behaviour notes
- Original bug fixed: block entity never restored the seat entity id (checked the wrong NBT type).
- Original quirk kept: WEIGHTED_WOODEN_HORSE and WALL_CUFF are not in the block entity's valid-block list.
- Forge-only `shouldRiderSit` has no equivalent; riding pose is handled in phase 2.
