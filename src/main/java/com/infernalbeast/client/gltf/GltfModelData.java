package com.infernalbeast.client.gltf;

import com.google.gson.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Minimal glTF 2.0 binary (GLB) reader for static triangle geometry and
 * node-based animation data. It deliberately keeps the original node
 * hierarchy: glTF animations target nodes, not the flattened OBJ export.
 *
 * This class is independent of Minecraft rendering so parsing can be tested
 * separately from the Forge renderer.
 */
public final class GltfModelData {
    public final JsonObject document;
    public final byte[] binary;
    public final List<Node> nodes = new ArrayList<>();
    public final List<Animation> animations = new ArrayList<>();

    private GltfModelData(JsonObject document, byte[] binary) {
        this.document = document;
        this.binary = binary;
        JsonArray nodeArray = document.getAsJsonArray("nodes");
        if (nodeArray != null) {
            for (int i = 0; i < nodeArray.size(); i++) nodes.add(new Node(i, nodeArray.get(i).getAsJsonObject()));
        }
        JsonArray animationArray = document.getAsJsonArray("animations");
        if (animationArray != null) {
            for (JsonElement element : animationArray) animations.add(new Animation(element.getAsJsonObject()));
        }
    }

    public static GltfModelData read(InputStream input) throws IOException {
        byte[] bytes = readAll(input);
        if (bytes.length < 20) throw new IOException("GLB is too small");
        ByteBuffer header = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        if (header.getInt(0) != 0x46546C67) throw new IOException("Not a glTF binary file");
        int version = header.getInt(4);
        if (version != 2) throw new IOException("Unsupported GLB version: " + version);
        int declaredLength = header.getInt(8);
        if (declaredLength > bytes.length || declaredLength < 20) throw new IOException("Invalid GLB length");

        int cursor = 12;
        JsonObject json = null;
        byte[] bin = new byte[0];
        while (cursor + 8 <= declaredLength) {
            int length = header.getInt(cursor);
            int type = header.getInt(cursor + 4);
            cursor += 8;
            if (length < 0 || cursor + length > declaredLength) throw new IOException("Invalid GLB chunk");
            if (type == 0x4E4F534A) {
                String text = new String(bytes, cursor, length, StandardCharsets.UTF_8).trim();
                json = JsonParser.parseString(text).getAsJsonObject();
            } else if (type == 0x004E4942) {
                bin = Arrays.copyOfRange(bytes, cursor, cursor + length);
            }
            cursor += length;
        }
        if (json == null) throw new IOException("GLB has no JSON chunk");
        return new GltfModelData(json, bin);
    }

    public String animationNames() {
        List<String> names = new ArrayList<>();
        for (Animation animation : animations) names.add(animation.name);
        return String.join(", ", names);
    }

    /** Reads a float accessor, including byte offsets and interleaved strides. */
    public float[] readFloatAccessor(int accessorIndex) throws IOException {
        JsonArray accessors = document.getAsJsonArray("accessors");
        JsonObject accessor = accessors.get(accessorIndex).getAsJsonObject();
        int count = accessor.get("count").getAsInt();
        String type = accessor.get("type").getAsString();
        int components = componentCount(type);
        float[] result = new float[count * components];
        if (!accessor.has("bufferView")) return result; // sparse-only accessors are not used by these assets
        JsonObject view = document.getAsJsonArray("bufferViews")
                .get(accessor.get("bufferView").getAsInt()).getAsJsonObject();
        int base = optionalInt(view, "byteOffset", 0) + optionalInt(accessor, "byteOffset", 0);
        int stride = optionalInt(view, "byteStride", components * 4);
        ByteBuffer data = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < count; i++) {
            for (int c = 0; c < components; c++) {
                int offset = base + i * stride + c * 4;
                if (offset < 0 || offset + 4 > binary.length) throw new IOException("Accessor outside GLB buffer");
                result[i * components + c] = data.getFloat(offset);
            }
        }
        return result;
    }

    private static int componentCount(String type) throws IOException {
        switch (type) {
            case "SCALAR": return 1;
            case "VEC2": return 2;
            case "VEC3": return 3;
            case "VEC4": return 4;
            case "MAT2": return 4;
            case "MAT3": return 9;
            case "MAT4": return 16;
            default: throw new IOException("Unsupported accessor type: " + type);
        }
    }

    private static int optionalInt(JsonObject object, String key, int fallback) {
        return object.has(key) ? object.get(key).getAsInt() : fallback;
    }

    private static byte[] readAll(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);
        return output.toByteArray();
    }

    public static final class Node {
        public final int index;
        public final String name;
        public final int mesh;
        public final int[] children;
        public final float[] translation;
        public final float[] rotation;
        public final float[] scale;

        private Node(int index, JsonObject json) {
            this.index = index;
            this.name = json.has("name") ? json.get("name").getAsString() : "node_" + index;
            this.mesh = optionalInt(json, "mesh", -1);
            this.children = json.has("children") ? toIntArray(json.getAsJsonArray("children")) : new int[0];
            this.translation = vector(json, "translation", new float[]{0, 0, 0});
            this.rotation = vector(json, "rotation", new float[]{0, 0, 0, 1});
            this.scale = vector(json, "scale", new float[]{1, 1, 1});
        }
    }

    public static final class Animation {
        public final String name;
        public final JsonArray samplers;
        public final JsonArray channels;

        private Animation(JsonObject json) {
            this.name = json.has("name") ? json.get("name").getAsString() : "unnamed";
            this.samplers = json.getAsJsonArray("samplers");
            this.channels = json.getAsJsonArray("channels");
        }
    }

    private static float[] vector(JsonObject json, String key, float[] fallback) {
        if (!json.has(key)) return fallback.clone();
        JsonArray values = json.getAsJsonArray(key);
        float[] result = new float[values.size()];
        for (int i = 0; i < result.length; i++) result[i] = values.get(i).getAsFloat();
        return result;
    }

    private static int[] toIntArray(JsonArray values) {
        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) result[i] = values.get(i).getAsInt();
        return result;
    }
}
