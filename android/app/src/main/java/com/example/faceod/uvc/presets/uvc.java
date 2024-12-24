package com.example.faceod.uvc.presets;

import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.tools.*;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;

@Properties(
        inherit = javacpp.class,
        target = "com.example.faceod.uvc",
        global = "com.example.faceod.uvc.global.uvc",
        value = {
    @Platform(include = {"libuvc/libuvc.h", "libuvc/libuvc_internal.h"}, link = "uvc"),
    @Platform(value = "android", link = "uvc"
    )
})
@NoException
public class uvc implements InfoMapper {

    public void map(InfoMap infoMap) {
        infoMap
                .put(new Info().enumerate())
                .put(new Info( "__ANDROID__").define())
                        .put(new Info( "UVC_DEBUGGING", "defined(__APPLE__) && defined(__MACH__)").define(false))
         .put(new Info("timeval", "timespec").cast().pointerTypes("Pointer"))
                .put(new Info("uvc_streaming_interface").cast().pointerTypes("Pointer"))
                .put(new Info("usb_dev", "transfers", "handler_thread", "config", "cb_cond", "cb_thread", "cb_mutex", "status_xfer").skip())
                .put(new Info("UVC_COLOR_FORMAT_NV12","UVC_COLOR_FORMAT_GRAY16","UVC_COLOR_FORMAT_GRAY8","UVC_COLOR_FORMAT_MJPEG","UVC_COLOR_FORMAT_BGR", "UVC_COLOR_FORMAT_RGB", "UVC_COLOR_FORMAT_UNKNOWN", "UVC_COLOR_FORMAT_UNCOMPRESSED", "UVC_COLOR_FORMAT_COMPRESSED", "UVC_COLOR_FORMAT_UYVY","UVC_COLOR_FORMAT_YUYV").skip())

     ;

    }

}