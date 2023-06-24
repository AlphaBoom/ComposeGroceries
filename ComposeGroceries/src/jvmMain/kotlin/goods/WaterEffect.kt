package goods

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.*
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import java.nio.ByteBuffer
import java.nio.ByteOrder


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


val SHADER_BRUSH_TEST = """
            uniform float time;
            
            float f(vec3 p) {
                p.z -= 10. + time;
                float a = p.z * .1;
                p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
                return .1 - length(cos(p.xy) + sin(p.yz));
            }
            
            half4 main(vec2 fragcoord) { 
                vec3 d = .5 - fragcoord.xy1 / 500;
                vec3 p=vec3(0);
                for (int i = 0; i < 32; i++) p += f(p) * d;
                return ((sin(p) + vec3(2, 5, 9)) / length(p)).xyz1;
            }
        """.trimIndent()

@Composable
fun ShaderBrush() {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }
    val runtimeEffect = RuntimeEffect.makeForShader(SHADER_BRUSH_TEST)
    val byteBuffer = remember { ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN) }
    val timeBits = (byteBuffer.clear() as ByteBuffer).putFloat(time).array()
    val shader = runtimeEffect.makeShader(
        uniforms = Data.makeFromBytes(timeBits),
        children = null,
        localMatrix = null
    )
    val brush = ShaderBrush(shader)

    Box(modifier = Modifier.fillMaxWidth().height(360.dp).drawBehind {
        drawRect(
            brush = brush, topLeft = Offset(100f, 65f), size = Size(400f, 400f)
        )
    })
}

/**
 * 通过Skia canvas将绘制好的数据保存到Image中，将该Image数据传入Shader进行处理
 * 其他：通过[Image]生成[Bitmap]需要拷贝，通过[Bitmap]生成[Image]不需要拷贝。
 * 状态：搁置
 */
@Composable
fun WaterRippleBySkiaCanvas(resourcePath: String) {
    val image = remember {
        useResource(resourcePath) {
            Image.makeFromEncoded(it.readAllBytes())
        }
    }
    val bitmap = remember { Bitmap.makeFromImage(image) }
    val paint = Paint()
    paint.shader = image.makeShader()
    val otherPaint = Paint()
    otherPaint.color = Color.Black.toArgb()
    otherPaint.strokeWidth = 3f
    Box(modifier = Modifier.fillMaxWidth().height(with(LocalDensity.current) { image.height.toDp() }).drawBehind {
        Canvas(bitmap).let {
            it.drawCircle(100f,100f, 50f, otherPaint)
        }
        drawIntoCanvas {
            it.nativeCanvas.clipRect(Rect.makeWH(image.width.toFloat(), image.height.toFloat()))
            it.nativeCanvas.drawImage(Image.makeFromBitmap(bitmap), 0f, 0f)
        }
    })
}

val WATER_RIPPLE = """
    uniform int hasShader;
    uniform float2 resolution;
    uniform vec3 iMouse;
    uniform shader contents;
    const float delta = 1.0;
    half4 main(in vec2 fragCoord) {
        if (hasShader == 0) {
            return vec4(0);
        }
        float pressure = contents.eval(fragCoord).x;
        float pVel = contents.eval(fragCoord).y;
        float p_right = contents.eval(fragCoord+vec2(1,0)).x;
        float p_left = contents.eval(fragCoord+vec2(-1,0)).x;
        float p_up = contents.eval(fragCoord+vec2(0,1)).x;
        float p_down = contents.eval(fragCoord+vec2(0,-1)).x;
        if (fragCoord.x == 0.5) p_left = p_right;
        if (fragCoord.x == resolution.x - 0.5) p_right = p_left;
	    if (fragCoord.y == 0.5) p_down = p_up;
        if (fragCoord.y == resolution.y - 0.5) p_up = p_down;
        // Apply horizontal wave function
        pVel += delta * (-2.0 * pressure + p_right + p_left) / 4.0;
        // Apply vertical wave function (these could just as easily have been one line)
        pVel += delta * (-2.0 * pressure + p_up + p_down) / 4.0;
    
        // Change pressure by pressure velocity
        pressure += delta * pVel;
    
        // "Spring" motion. This makes the waves look more like water waves and less like sound waves.
        pVel -= 0.005 * delta * pressure;
    
        // Velocity damping so things eventually calm down
        pVel *= 1.0 - 0.002 * delta;
    
        // Pressure damping to prevent it from building up forever.
        pressure *= 0.999;
    
        //x = pressure. y = pressure velocity. Z and W = X and Y gradient
        vec4 fragColor = vec4(pressure, pVel, (p_right - p_left) / 2.0, (p_up - p_down) / 2.0);
        if (iMouse.z >= 1.0) {
            float dist = distance(fragCoord, iMouse.xy);
            if (dist <= 20.0) {
                fragColor.x += 1.0 - dist / 20.0;
            }
        }
        return fragColor;
    }
""".trimIndent()

val WATER_LIGHT = """
    uniform float2 resolution;
    uniform float time;
    uniform shader ripple;
    uniform shader contents;
    half4 main(in vec2 fragCoord) {
        vec2 uv = fragCoord/resolution.xy;
        vec4 data = ripple.eval(fragCoord);
        vec4 fragColor = contents.eval((uv + 0.2 * data.zw)*resolution.xy);
        vec3 normal = normalize(vec3(-data.z, 0.2, -data.w));
        fragColor += vec4(1) * pow(max(0.0, dot(normal, normalize(vec3(-3, 10, 3)))), 60.0);
        return fragColor;
    }
""".trimIndent()

var buffer: ImageFilter? = null

/**
 * 能够实现效果，但是该实现会Stack Overflow，因为实际是通过嵌套多个shader达成的，这种方式是不可行的。
 * 另外一个尝试方式是 [WaterRippleBySkiaCanvas]，直接在画布上绘制，通过给[Paint]设置[Shader]的方式使用着色器，数据共享通过[Image]。
 * 当前结论：如果需要这类效果实现，直接通过OpenGL来实现会更方便。
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WaterRippleByRenderEffect(resourcePath: String) {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }
    val ripple = remember { RuntimeEffect.makeForShader(WATER_RIPPLE) }
    val light = remember { RuntimeEffect.makeForShader(WATER_LIGHT) }
    val rippleShaderBuilder = remember { RuntimeShaderBuilder(ripple) }
    val lightShaderBuilder = remember { RuntimeShaderBuilder(light) }
    Image(painterResource(resourcePath), "", modifier = Modifier.onPointerEvent(PointerEventType.Move) {
        val first = it.changes.first()
        rippleShaderBuilder.uniform(
            "iMouse", first.position.x, first.position.y, if (first.pressed) {
                1f
            } else {
                0f
            }
        )
    }.onSizeChanged { size ->
        rippleShaderBuilder.uniform(
            "resolution",
            size.width.toFloat(),
            size.height.toFloat()
        )
        lightShaderBuilder.uniform(
            "resolution",
            size.width.toFloat(),
            size.height.toFloat()
        )
    }.graphicsLayer {
        rippleShaderBuilder.uniform(
            "hasShader", if (buffer == null) {
                0
            } else {
                1
            }
        )
        time
        val rippleShader = ImageFilter.makeRuntimeShader(rippleShaderBuilder, "contents", buffer)
        buffer = rippleShader
        val lightShader = ImageFilter.makeRuntimeShader(lightShaderBuilder, arrayOf("contents", "ripple"), arrayOf(null,buffer))
        renderEffect = lightShader.asComposeRenderEffect()
    })
}


private enum class EffectType {
    PerlinNoise,
    Original,
    WaterRipple,
    ShaderBrush,
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WaterEffect() {
    val types = remember {
        arrayOf(
            EffectType.PerlinNoise,
            EffectType.Original,
            EffectType.ShaderBrush
        )
    }
    var current by remember { mutableStateOf(types[0]) }
    val resourceName = "miku.jpg"
    Column {
        when (current) {
            EffectType.PerlinNoise -> PerlinNoise(resourceName)
            EffectType.Original -> Image(painterResource(resourceName), null)
            EffectType.WaterRipple -> WaterRippleByRenderEffect(resourceName)
            EffectType.ShaderBrush -> ShaderBrush()
        }
        FlowRow {
            types.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = it == current, { current = it })
                    Text(it.name)
                }
            }
        }
    }

}