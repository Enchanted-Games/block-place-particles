#version 330
//? if minecraft: >= 26.3 {
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:oit.glsl>

uniform sampler2D Sampler0;
uniform sampler2D MaskSampler;

layout(location = 0) in float sphericalVertexDistance;
layout(location = 1) in float cylindricalVertexDistance;
layout(location = 2) in vec2 texCoord0;
layout(location = 3) in vec2 texCoordMask;
layout(location = 4) in vec4 vertexColor;

#ifndef OIT_ALPHA_ONLY
layout(location = 0) out vec4 fragColor;
#endif

vec4 calculateFinalColor(vec4 color) {
    #ifdef OIT_ACCUMULATE
    color = sampleColorForAccumulation(color);
    vec4 fogColor = vec4(FogColor.rgb * color.a, FogColor.a);
    #else
    vec4 fogColor = FogColor;
    #endif
    return apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, fogColor);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    color.a *= texture(MaskSampler, texCoordMask).a;
    if (color.a < 0.1) {
        discard;
    }
    #ifdef OIT_ALPHA_ONLY
    executeAlphaOnlyPhase(gl_FragCoord.z, color.a);
    #else
    fragColor = calculateFinalColor(color);
    #endif
}

//? } else {
//#moj_import <minecraft:fog.glsl>
//#moj_import <minecraft:dynamictransforms.glsl>
//
//uniform sampler2D Sampler0;
//uniform sampler2D MaskSampler;
//
//in float sphericalVertexDistance;
//in float cylindricalVertexDistance;
//in vec2 texCoord0;
//in vec2 texCoordMask;
//in vec4 vertexColor;
//
//out vec4 fragColor;
//
//void main() {
//    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
//    color.a *= texture(MaskSampler, texCoordMask).a;
//    if (color.a < 0.1) {
//        discard;
//    }
//    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
//}
//?}
