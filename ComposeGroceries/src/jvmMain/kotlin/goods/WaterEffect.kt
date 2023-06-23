package goods

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder


/* Copyright 2022 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
val PERLIN_NOISE = """
   uniform float2 resolution;
   uniform float time;
   uniform shader contents;
  
   //
   // Description : Array and textureless GLSL 2D/3D/4D simplex
   //               noise functions.
   //      Author : Ian McEwan, Ashima Arts.
   //  Maintainer : stegu
   //     Lastmod : 20201014 (stegu)
   //     License : Copyright (C) 2011 Ashima Arts. All rights reserved.
   //               Distributed under the MIT License. See LICENSE file.
   //               https://github.com/ashima/webgl-noise
   //               https://github.com/stegu/webgl-noise
   //
  
   vec3 mod289(vec3 x) {
    return x - floor(x * (1.0 / 289.0)) * 289.0;
   }
  
   vec4 mod289(vec4 x) {
    return x - floor(x * (1.0 / 289.0)) * 289.0;
   }
  
   vec4 permute(vec4 x) {
       return mod289(((x*34.0)+10.0)*x);
   }
  
   float snoise(vec3 v)
   {
    const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;
    const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);
  
     // First corner
    vec3 i  = floor(v + dot(v, C.yyy) );
    vec3 x0 =   v - i + dot(i, C.xxx) ;
  
    // Other corners
    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min( g.xyz, l.zxy );
    vec3 i2 = max( g.xyz, l.zxy );
  
    //   x0 = x0 - 0.0 + 0.0 * C.xxx;
    //   x1 = x0 - i1  + 1.0 * C.xxx;
    //   x2 = x0 - i2  + 2.0 * C.xxx;
    //   x3 = x0 - 1.0 + 3.0 * C.xxx;
    vec3 x1 = x0 - i1 + C.xxx;
    vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y
    vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y
  
    // Permutations
    i = mod289(i);
    vec4 p = permute( permute( permute(
               i.z + vec4(0.0, i1.z, i2.z, 1.0 ))
             + i.y + vec4(0.0, i1.y, i2.y, 1.0 ))
             + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));
  
    // Gradients: 7x7 points over a square, mapped onto an octahedron.
    // The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)
    float n_ = 0.142857142857; // 1.0/7.0
    vec3  ns = n_ * D.wyz - D.xzx;
  
    vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)
  
    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)
  
    vec4 x = x_ *ns.x + ns.yyyy;
    vec4 y = y_ *ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);
  
    vec4 b0 = vec4( x.xy, y.xy );
    vec4 b1 = vec4( x.zw, y.zw );
  
    vec4 s0 = floor(b0)*2.0 + 1.0;
    vec4 s1 = floor(b1)*2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));
  
    vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;
    vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;
  
    vec3 p0 = vec3(a0.xy,h.x);
    vec3 p1 = vec3(a0.zw,h.y);
    vec3 p2 = vec3(a1.xy,h.z);
    vec3 p3 = vec3(a1.zw,h.w);
  
    //Normalise gradients
    vec4 norm = inversesqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));
    p0 *= norm.x;
    p1 *= norm.y;
    p2 *= norm.z;
    p3 *= norm.w;
  
    // Mix final noise value
    vec4 m = max(0.5 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
    m = m * m;
    return 105.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1),
                                  dot(p2,x2), dot(p3,x3) ) );
   }
  
   half4 main(in vec2 fragCoord) {
      vec2 uv = (fragCoord.xy / resolution.xy);
      float noise = snoise(vec3(uv.x * 6, uv.y * 6, time * 0.5));
     
      noise *= exp(-length(abs(uv * 1.5))); 
      vec2 offset1 = vec2(noise * 0.02);
      vec2 offset2 = vec2(0.02) / resolution.xy;
      uv += offset1 - offset2;
     
      return contents.eval(uv * resolution.xy);
   }
""".trimIndent()

@Composable
fun PerlinNoise(resourcePath: String) {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }
    val effect = remember { RuntimeEffect.makeForShader(PERLIN_NOISE) }
    val shader = remember { RuntimeShaderBuilder(effect) }
    Image(painterResource(resourcePath), "", modifier = Modifier.onSizeChanged { size ->
        shader.uniform(
            "resolution",
            size.width.toFloat(),
            size.height.toFloat()
        )
    }.graphicsLayer {
        shader.uniform("time", time)
        renderEffect = ImageFilter.makeRuntimeShader(shader, "contents", null).asComposeRenderEffect()
    })
}

private enum class EffectType {
    PerlinNoise,
    Original
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WaterEffect() {
    val types = remember { arrayOf(EffectType.PerlinNoise, EffectType.Original) }
    var current by remember { mutableStateOf(types[0]) }
    val resourceName = "miku.jpg"
    Column {
        when (current) {
            EffectType.PerlinNoise -> PerlinNoise(resourceName)
            EffectType.Original -> Image(painterResource(resourceName), null)
        }
        FlowRow {
            types.forEach {
                Row(verticalAlignment = Alignment.CenterVertically){
                    RadioButton(selected = it == current, { current = it })
                    Text(it.name)
                }
            }
        }
    }

}