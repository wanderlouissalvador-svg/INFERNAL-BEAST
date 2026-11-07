# Predator GLB rigging and GeckoLib handoff

## Source asset
The source predator is a single fused GLB mesh. It contains no skin/skeleton and no animation clips. A rig cannot be considered complete until the generated result has been opened and inspected in Blender.

## First-pass rig
Run from the repository root with Blender 3.6 or newer:

```bash
blender --background --python tools/rig_predator.py -- path/to/predator.glb build/predator_rigged.glb
```

The script creates a provisional armature for the root, body, chest, neck, head, jaw, two forelimbs, and eight walking legs. It then asks Blender to calculate automatic weights and exports a skinned GLB.

## Required validation before Minecraft integration
1. Open the exported GLB in Blender and inspect the model in rest pose.
2. Rotate each leg, forelimb, jaw, head, and dorsal appendage separately. Check for stretching, detached-looking regions, and weight bleed.
3. Correct bone placement and weight painting. The source model's normalized dimensions and coordinate orientation may differ from the provisional assumptions in the script.
4. Confirm the model faces the intended forward axis and stands upright; apply a consistent scale and origin.
5. Export a copy for archival. Do not overwrite the original source GLB.

## GeckoLib integration gate
GeckoLib 4.8.4 for Forge 1.20.1 does not directly render arbitrary GLB files as entity models. The rigged GLB is an intermediate asset. The final Minecraft asset must be converted into GeckoLib's geometry and animation formats (typically `.geo.json` and `.animation.json`) with compatible bone names and texture/material setup.

Planned animation clips after the rig passes validation:
- `idle`: breathing, head tracking, subtle appendage motion.
- `walk` and `run`: alternating leg gait with body bob.
- `stalk`: low body posture and slow head movement.
- `leap`: forelimb wind-up, body launch, landing.
- `attack`: claw strike and jaw motion.
- `devour`: pin, bite, and recovery.
- `death`: collapse and final appendage movement.

## Important
The current rig builder is an automated first pass, not a finished production rig. Do not advertise the predator as fully animated or Minecraft-ready until Blender validation, GeckoLib conversion, and a Forge 1.20.1 test build all pass.
