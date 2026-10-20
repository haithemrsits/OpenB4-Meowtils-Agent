#!/usr/bin/env bash
# Usage: ./build.sh /path/to/RavenB4-u20+.jar
# Needs JDK 11+ (javac --release 8), python3, unzip, jar.
set -euo pipefail
IN="$(realpath "$1")"; ROOT="$(cd "$(dirname "$0")" && pwd)"; W="$ROOT/work"
rm -rf "$W"; mkdir -p "$W"/{orig,stubs,tool,patched,shim,mix,out} "$ROOT/dist"
J="javac --release 8 -nowarn"
unzip -q -o "$IN" -d "$W/orig"
$J -d "$W/stubs" $(find "$ROOT/stubs" -name '*.java')
# the bundled Mixin/ASM inside the original jar is used to compile and run the patcher
$J -cp "$W/orig" -d "$W/tool" "$ROOT/tools/Patch.java"
java -cp "$W/orig:$W/tool" Patch "$W/orig" "$W/patched"
$J -cp "$W/stubs:$W/patched" -d "$W/shim" $(find "$ROOT/src/shim" -name '*.java')
$J -cp "$W/stubs:$W/shim:$W/patched" -d "$W/mix" $(find "$ROOT/src/mixins" -name '*.java')
cp -r "$W/patched/." "$W/out/"; cp -r "$W/shim/." "$W/out/"; cp -r "$W/mix/." "$W/out/"
python3 -I - "$W/out" "$ROOT/src/lunar.refmap.part.json" <<'PY'
import json,os,sys
out,part=sys.argv[1],sys.argv[2]
old=json.load(open(out+'/clickgui.mixins.json'))
lunar=sorted("lunar."+f[:-6] for f in os.listdir(out+'/xyz/daikirai/mixin/lunar') if f.endswith('.class'))
cfg={"required":False,"package":"xyz.daikirai.mixin","refmap":"mixins.ravenb4.refmap.json","minVersion":"0.6",
 "compatibilityLevel":"JAVA_8","priority":10000,"mixinPriority":10000,"defaultRequire":0,"injectors":{"defaultRequire":0},
 "client":old["client"]+lunar}
json.dump(cfg,open(out+'/mixins.ravenb4.json','w'),indent=2)
rm=json.load(open(out+'/clickgui.refmap.json'))
for k,v in json.load(open(part)).items():
    rm['mappings'][k]=v
    for ctx in rm.get('data',{}).values(): ctx[k]=v
json.dump(rm,open(out+'/mixins.ravenb4.refmap.json','w'),indent=1)
os.remove(out+'/clickgui.refmap.json'); os.remove(out+'/clickgui.mixins.json')
json.dump({"version":"u20+"},open(out+'/version.json','w'))
PY
printf 'Manifest-Version: 1.0\nTweakClass: org.spongepowered.asm.launch.MixinTweaker\nTweakOrder: 0\nMixinConfigs: mixins.ravenb4.json\nFMLCorePluginContainsFMLMod: true\nForceLoadAsMod: true\n\n' > "$W/out/META-INF/MANIFEST.MF"
rm -f "$ROOT/dist/RavenB4.jar"
( cd "$W/out" && jar cfm "$ROOT/dist/RavenB4.jar" META-INF/MANIFEST.MF $(ls | grep -v META-INF) META-INF/services )
echo "built $ROOT/dist/RavenB4.jar"
