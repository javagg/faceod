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
    @Platform(include = {"libuvc/libuvc.h"}, link = "uvc"),
    @Platform(value = "android", link = "uvc"
    )
})
@NoException
public class uvc implements InfoMapper {

    public void map(InfoMap infoMap) {
        infoMap.put(new Info("__cplusplus").define())
        .put(new Info("timeval", "timespec").cast().pointerTypes("Pointer"))
                .put(new Info("uvc_streaming_interface").cast().pointerTypes("Pointer"))

                .put(new Info("uvc_still_frame_desc_t").pointerTypes("uvc_still_frame_desc"))
        .put(new Info("uvc_input_terminal_t").pointerTypes("uvc_input_terminal"))
                .put(new Info("uvc_selector_unit_t").pointerTypes("uvc_selector_unit"))
                .put(new Info("uvc_output_terminal_t").pointerTypes("uvc_output_terminal"))
                .put(new Info("uvc_processing_unit_t").pointerTypes("uvc_processing_unit"))
                .put(new Info("uvc_extension_unit_t").pointerTypes("uvc_extension_unit"))
                .put(new Info("uvc_extension_unit_t").pointerTypes("uvc_extension_unit"))
                .put(new Info("uvc_frame_t").pointerTypes("uvc_frame"))

                .put(new Info("uvc_still_frame_res_t").pointerTypes("uvc_still_frame_res"));


//        infoMap.put(new Info("ZEXTERN", "ZEXPORT", "z_const", "zlib_version").cppTypes().annotations())
//                .put(new Info("FAR").cppText("#define FAR"))
//                .put(new Info("OF").cppText("#define OF(args) args"))
//                .put(new Info("Z_ARG").cppText("#define Z_ARG(args) args"))
//                .put(new Info("Byte", "Bytef", "charf").cast().valueTypes("byte").pointerTypes("BytePointer"))
//                .put(new Info("uInt", "uIntf").cast().valueTypes("int").pointerTypes("IntPointer"))
//                .put(new Info("uLong", "uLongf", "z_crc_t", "z_off_t", "z_size_t").cast().valueTypes("long").pointerTypes("CLongPointer"))
//                .put(new Info("z_off64_t").cast().valueTypes("long").pointerTypes("LongPointer"))
//                .put(new Info("voidp", "voidpc", "voidpf").valueTypes("Pointer"))
//                .put(new Info("gzFile_s").pointerTypes("gzFile"))
//                .put(new Info("gzFile").valueTypes("gzFile"))
//                .put(new Info("Z_LARGE64", "!defined(ZLIB_INTERNAL) && defined(Z_WANT64)").define(false))
//                .put(new Info("inflateGetDictionary", "gzopen_w", "gzvprintf").skip());
    }

}