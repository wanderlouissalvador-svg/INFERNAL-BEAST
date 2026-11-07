Dark Stalker GLB integration - asset contract

The client-side loader is being built around the original glTF 2.0 binary model.
Expected resource path:
  assets/infernal_beast/models/entity/dark_stalker.glb

The GLB must be copied byte-for-byte from the user's original dark_stalker.glb.
Do not use the flattened OBJ as the animation source: glTF animation channels
target the original node hierarchy, which the OBJ export does not preserve.

The first implementation step adds GltfModelData, which reads the GLB JSON and
BIN chunks, preserves node transforms/children, exposes animation names and
reads float accessors used by animation samplers.

Not yet implemented in this commit:
- triangle primitive decoding and GPU buffer upload;
- node transform evaluation and animation interpolation at runtime;
- material/texture decoding (the source embeds a WebP image);
- renderer registration and in-game validation.

Do not replace the existing renderer until those steps are complete.
