# INFERNAL-BEAST

Base inicial del mod para Minecraft Java 1.20.1 + Forge 47.4.10 + Java 17, con GeckoLib 4.

## Estado
- Estructura base de Forge.
- GeckoLib configurado como dependencia.
- Compilación automática mediante GitHub Actions (en preparación).
- Pendiente: conversión de modelos GLB a geometría/animaciones GeckoLib, entidades, IA, ataques, gólem guardián y katanas.

Los modelos GLB no se cargan directamente como entidades de Minecraft: deben convertirse a geometría, texturas y animaciones compatibles.

## Compilar
Ejecuta `gradle build` con Java 17. El JAR se genera en `build/libs/`.

Mod ID: `infernal_beast`
