// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {

    return {
        'modelpathing': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.resources.model.ModelBakery',
                'methodName': 'processLoading', // Added by Forge
                'methodDesc': '(Lnet/minecraft/util/profiling/ProfilerFiller;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var lastInstruction = null;
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        node.name === ASM.mapField("f_122827_") // ITEM

                    )
                        lastInstruction = node;
                }
                if (lastInstruction != null)
                    instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'tamaized/frostfell/asm/ASMHooks',
                                'redirectModels',
                                '()V',
                                false
                                )
                            )
                        );
                instructions.insertBefore(
                    ASM.findFirstMethodCall(methodNode, ASM.MethodType.STATIC,
                        "com/google/common/collect/Sets",
                        "newLinkedHashSet",
                        "()Ljava/util/LinkedHashSet;"),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'tamaized/frostfell/asm/ASMHooks',
                            'cleanModels',
                            '()V',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
