package com.example.faceod

import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.view.TextureRegistry

class MainActivity: FlutterActivity(), MethodCallHandler {

  private lateinit var channel : MethodChannel
  private lateinit var textureRegistry: TextureRegistry
  private lateinit var surfaceEntry: TextureRegistry.SurfaceTextureEntry


  override fun onMethodCall(call: MethodCall, result: Result) {
    val arguments = call.arguments<Map<String,Int>>()
    when (call.method) {
      "createTexture" -> {
        val width = arguments!!["width"] 
        val height = arguments!!["height"]
        surfaceEntry = textureRegistry.createSurfaceTexture()
        val surfaceTexture = surfaceEntry.surfaceTexture().apply {
          setDefaultBufferSize(width!!, height!!)
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
