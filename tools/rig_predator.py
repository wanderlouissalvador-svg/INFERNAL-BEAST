# INFERNAL-BEAST - first-pass rig builder for the Copilot3D predator GLB
# Run with Blender 3.6+:
# blender --background --python rig_predator.py -- input.glb output_rigged.glb
import bpy, sys
from mathutils import Vector

args = sys.argv[sys.argv.index("--")+1:] if "--" in sys.argv else []
if len(args) < 2:
    raise SystemExit("Usage: blender --background --python rig_predator.py -- input.glb output_rigged.glb")
src, dst = args[0], args[1]
bpy.ops.object.select_all(action="SELECT")
bpy.ops.object.delete(use_global=False)
bpy.ops.import_scene.gltf(filepath=src)
meshes = [o for o in bpy.context.scene.objects if o.type == "MESH"]
if not meshes:
    raise RuntimeError("The GLB contains no mesh objects.")
bpy.ops.object.select_all(action="DESELECT")
for obj in meshes: obj.select_set(True)
bpy.context.view_layer.objects.active = meshes[0]
if len(meshes) > 1: bpy.ops.object.join()
mesh = bpy.context.view_layer.objects.active
mesh.name = "Predator_Body_Rigged"
bpy.ops.object.transform_apply(location=False, rotation=True, scale=True)
arm_data = bpy.data.armatures.new("Predator_Armature")
arm = bpy.data.objects.new("Predator_Armature", arm_data)
bpy.context.collection.objects.link(arm)
bpy.context.view_layer.objects.active = arm
arm.select_set(True); mesh.select_set(False)
bpy.ops.object.mode_set(mode="EDIT")
bones = {}
def bone(name, head, tail, parent=None):
    b = arm_data.edit_bones.new(name)
    b.head, b.tail = Vector(head), Vector(tail)
    if parent: b.parent = bones[parent]
    bones[name] = b
bone("root",(0,-.34,0),(0,-.20,0))
bone("body",(0,-.20,0),(0,.02,0),"root")
bone("chest",(0,.02,0),(0,.17,-.10),"body")
bone("neck",(0,.13,-.10),(0,.22,-.22),"chest")
bone("head",(0,.22,-.22),(0,.28,-.34),"neck")
bone("jaw",(0,.20,-.29),(0,.16,-.39),"head")
for side,sx in (("L",1),("R",-1)):
    bone("claw_"+side,(sx*.12,.08,-.12),(sx*.30,.02,-.24),"chest")
    bone("forearm_"+side,(sx*.30,.02,-.24),(sx*.39,-.10,-.31),"claw_"+side)
    bone("hand_"+side,(sx*.39,-.10,-.31),(sx*.44,-.16,-.36),"forearm_"+side)
    for i,z in enumerate((.20,.07,-.08,-.22),1):
        hip=(sx*.10,-.10,z)
        knee=(sx*(.27+.025*i),-.18,z+(.035 if i<3 else -.035))
        foot=(sx*(.43+.025*i),-.34,z+(.07 if i<3 else -.07))
        bone(f"leg{i}_{side}",hip,knee,"body")
        bone(f"shin{i}_{side}",knee,foot,f"leg{i}_{side}")
        bone(f"foot{i}_{side}",foot,(foot[0]+sx*.04,foot[1]-.01,foot[2]),f"shin{i}_{side}")
    bone("back_spike_"+side,(sx*.04,.13,.02),(sx*.18,.30,.12),"chest")
bpy.ops.object.mode_set(mode="OBJECT")
bpy.ops.object.select_all(action="DESELECT")
mesh.select_set(True); arm.select_set(True)
bpy.context.view_layer.objects.active = arm
try:
    bpy.ops.object.parent_set(type="ARMATURE_AUTO")
except Exception as exc:
    raise RuntimeError("Automatic weighting failed; manual weight painting is required: "+str(exc))
bpy.ops.object.select_all(action="DESELECT")
mesh.select_set(True); arm.select_set(True)
bpy.context.view_layer.objects.active = arm
bpy.ops.export_scene.gltf(filepath=dst, export_format="GLB", use_selection=True,
                          export_animations=False, export_skins=True)
print("Exported first-pass rig:", dst)
