#version 330
//? if minecraft: >= 26.3 {
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:sample_lightmap.glsl>

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV0;
layout(location = 2) in vec2 MaskUV;
layout(location = 3) in vec4 Color;
layout(location = 4) in ivec2 UV2;

uniform sampler2D Sampler2;

layout(location = 0) out float sphericalVertexDistance;
layout(location = 1) out float cylindricalVertexDistance;
layout(location = 2) out vec2 texCoord0;
layout(location = 3) out vec2 texCoordMask;
layout(location = 4) out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    texCoord0 = UV0;
    texCoordMask = MaskUV;
    vertexColor = Color * sample_lightmap(Sampler2, UV2);
}
//? } else {
//#moj_import <minecraft:fog.glsl>
//#moj_import <minecraft:dynamictransforms.glsl>
//#moj_import <minecraft:projection.glsl>
//#moj_import <minecraft:sample_lightmap.glsl>
//
//in vec3 Position;
//in vec2 UV0;
//in vec2 MaskUV;
//in vec4 Color;
//in ivec2 UV2;
//
//uniform sampler2D Sampler2;
//
//out float sphericalVertexDistance;
//out float cylindricalVertexDistance;
//out vec2 texCoord0;
//out vec2 texCoordMask;
//out vec4 vertexColor;
//
//void main() {
//    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
//
//    sphericalVertexDistance = fog_spherical_distance(Position);
//    cylindricalVertexDistance = fog_cylindrical_distance(Position);
//    texCoord0 = UV0;
//    texCoordMask = MaskUV;
//    vertexColor = Color * sample_lightmap(Sampler2, UV2);
//}
//? }
