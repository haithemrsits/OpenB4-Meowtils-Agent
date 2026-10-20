# Raven B4 (OpenB4) -> Lunar Client 1.8.9 port layer

Runs Raven B4 `u20+` on Lunar Client 1.8.9 (no Forge) through the Lunar agent (`agent-mods.json`).
This repo contains only the port layer; the Raven B4 jar itself is the input and is not included.

## How it works

* `src/shim` - a small Forge API stand-in relocated to `xyz.daikirai.forge.\*` (EventBus, TickEvent, MouseEvent, ...),
`lunar/LunarBootstrap` (replaces the `@Mod` lifecycle), `lunar/Events` (mixin -> event bus glue), `lunar/Safe` (guards for JRE-only runtimes).
* `src/mixins` - mixins that fire the Forge-style events vanilla never emits. MCP names + refmap (`src/lunar.refmap.part.json`),
same scheme as Raven's own mixins, which is what the Lunar agent expects.
* `tools/Patch.java` - build-time ASM patcher: relocates `net.minecraftforge.\*` references in `xyz/\*\*` classes,
rewrites Forge-added vanilla calls (`Item.getRegistryName`, ...) to `Compat` statics, guards `ToolProvider` / `setAccessible`.
* `tools/LoaderPatch.java` - clones the Meowtils loader (input: an existing Meowtils loader extension) into a Raven B4 loader.
* `stubs/` - compile-only Minecraft/LWJGL signatures (never packaged).
* `test/SmokeTest.java` - EventBus behaviour test (run against the compiled shim).

## Build

&#x20;   ./build.sh /path/to/RavenB4-u20+.jar      # -> dist/RavenB4.jar


`agent-mods.json` entry:

&#x20;   { "jar": "<path>\\\\RavenB4.jar", "mixin": "mixins.ravenb4.json", "property": "ravenb4.agent.injected" }


## Status / known limits

Tested in-game on Lunar 1.8.9: startup, ClickGUI, chat commands. Not every module/event path has been verified.
Scripts need a JDK compiler and are disabled on a plain JRE. Several event hooks use method names written from memory;
a wrong target is skipped silently (`required:false`, `defaultRequire:0`) - check the log if a module does nothing.

## 

