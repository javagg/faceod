package com.example.faceod

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.TextureRegistry

class MainActivity: FlutterActivity(), MethodCallHandler {

  private lateinit var channel : MethodChannel
  private lateinit var textureRegistry: TextureRegistry
  private lateinit var surfaceEntry: TextureRegistry.SurfaceTextureEntry


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    val arguments = call.arguments<Map<String,Int>>()
    when (call.method) {
      "createTexture" -> {
        val width = arguments["width"] ?? 300
        val height = arguments["height"] ?? 300
        surfaceEntry = textureRegistry.createSurfaceTexture()
        val surfaceTexture = surfaceEntry.surfaceTexture().apply {
          setDefaultBufferSize(width, height)
        }

  //      externalGLThread = ExternalGLThread(surfaceTexture, SimpleRenderer())
 //       externalGLThread.start()

        result.success(surfaceEntry.id())
      }
      "disposeTexture" -> {
     //   externalGLThread.dispose()
        surfaceEntry.release()
      }
      else -> result.notImplemented()
    }
  }
}
