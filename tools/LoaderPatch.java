import java.io.*; import java.nio.file.*; import java.util.*;
import org.spongepowered.asm.lib.*; import org.spongepowered.asm.lib.commons.*;

/** Clones the proven Myau loader classes into a RavenB4 loader (renames, strings, hard-coded fallback config path). */
public class LoaderPatch {
    static final String FALLBACK = "C:\\Users\\Strange\\Pictures\\Meowtils-Lunar\\Meowtils-Lunar\\agent-mods.json";
    static String str(String s) {
        switch (s) {
            case "/meowtils/myau/Myau.jar": return "/meowtils/ravenb4/RavenB4.jar";
            case "Myau.jar": return "RavenB4.jar";
            case "myau.Myau": return "xyz.daikirai.lunar.LunarBootstrap";
            case "mixins.myau.json": return "mixins.ravenb4.json";
            case "myau.agent.injected": return "ravenb4.agent.injected";
            case "Mega": return "RavenB4 Port";
            case "Myau extension is only for Lunar. Just download OpenMyau manually to use it on Forge.":
                return "Raven B4 extension is only for Lunar. On Forge use the original Raven B4 jar.";
        }
        return s.replace("Myau.jar", "RavenB4.jar").replace("Myau", "Raven B4");
    }
    static final Remapper RM = new Remapper() {
        public String map(String n) { return n.startsWith("meowtils/myau/") ? n.replace("meowtils/myau/", "meowtils/ravenb4/").replace("Myau", "RavenB4") : n; }
    };
    static class CV extends ClassVisitor {
        CV(ClassVisitor c) { super(Opcodes.ASM5, c); }
        public FieldVisitor visitField(int a, String n, String d, String s, Object v) { return super.visitField(a, n, d, s, v instanceof String ? str((String) v) : v); }
        public MethodVisitor visitMethod(int a, final String mn, String d, String s, String[] e) {
            MethodVisitor mv = super.visitMethod(a, mn, d, s, e);
            return new MethodVisitor(Opcodes.ASM5, mv) {
                boolean pendingNull;
                public void visitLdcInsn(Object c) { flush(); super.visitLdcInsn(c instanceof String ? str((String) c) : c); }
                void flush() { if (pendingNull) { pendingNull = false; super.visitInsn(Opcodes.ACONST_NULL); } }
                public void visitInsn(int op) {
                    if (mn.equals("lunarAgentConfig")) {
                        if (op == Opcodes.ACONST_NULL) { flush(); pendingNull = true; return; }
                        if (op == Opcodes.ARETURN && pendingNull) {
                            pendingNull = false;
                            super.visitTypeInsn(Opcodes.NEW, "java/io/File"); super.visitInsn(Opcodes.DUP);
                            super.visitLdcInsn(FALLBACK);
                            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
                            super.visitInsn(Opcodes.ARETURN); System.out.println("patched fallback path"); return;
                        }
                    }
                    flush(); super.visitInsn(op);
                }
                public void visitLabel(Label l) { flush(); super.visitLabel(l); }
                public void visitJumpInsn(int o, Label l) { flush(); super.visitJumpInsn(o, l); }
                public void visitVarInsn(int o, int v) { flush(); super.visitVarInsn(o, v); }
                public void visitMethodInsn(int o, String ow, String n, String d, boolean i) { flush(); super.visitMethodInsn(o, ow, n, d, i); }
                public void visitFieldInsn(int o, String ow, String n, String d) { flush(); super.visitFieldInsn(o, ow, n, d); }
                public void visitTypeInsn(int o, String t) { flush(); super.visitTypeInsn(o, t); }
            };
        }
    }
    public static void main(String[] a) throws Exception {
        Path in = Paths.get(a[0]), out = Paths.get(a[1]);
        for (File f : in.resolve("meowtils/myau").toFile().listFiles()) {
            if (!f.getName().endsWith(".class")) continue;
            ClassReader cr = new ClassReader(Files.readAllBytes(f.toPath()));
            ClassWriter cw = new ClassWriter(0);
            cr.accept(new ClassRemapper(new CV(cw), RM), 0);
            String nn = f.getName().replace("Myau", "RavenB4");
            Files.createDirectories(out.resolve("meowtils/ravenb4"));
            Files.write(out.resolve("meowtils/ravenb4/" + nn), cw.toByteArray());
        }
    }
}
