import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

enum UVCCameraState { opened, closed, error }

enum VideoDataType { aac, h264Key, h264Sps, h264 }

class UVCCameraViewParamsEntity {
  /**
   *  if give custom minFps or maxFps or unsupported preview size
   *  set preview possible will fail
   *  **/
  /// camera preview min fps  10
  final int? minFps;

  /// camera preview max fps  60
  final int? maxFps;

  /// camera preview frame format 1 (MJPEG) or 0 (YUV)
  /// DEFAULT 1(MJPEG)  If preview fails and the screen goes black, please try switching to 0
  final int? frameFormat;

  ///  DEFAULT_BANDWIDTH = 1
  final double? bandwidthFactor;

  const UVCCameraViewParamsEntity({
    this.minFps = 10,
    this.maxFps = 60,
    this.bandwidthFactor = 1.0,
    this.frameFormat = 1,
  });

  Map<String, dynamic> toMap() {
    return {
      "minFps": minFps,
      "maxFps": maxFps,
      "frameFormat": frameFormat,
      "bandwidthFactor": bandwidthFactor
    };
  }
}

class PreviewSize {
  int? width;
  int? height;
  PreviewSize({this.width, this.height});

  Map<String, dynamic> toMap() {
    return {"width": width, "height": height};
  }

  PreviewSize.fromJson(dynamic json) {
    width = json["width"];
    height = json["height"];
  }

  @override
  String toString() {
    return 'PreviewSize{width: $width, height: $height}';
  }
}

class UVCCameraController {
  static const String _channelName = "flutter_uvc_camera/channel";

  UVCCameraState _cameraState = UVCCameraState.closed;

  /// 摄像头状态回调
  Function(UVCCameraState)? cameraStateCallback;

  /// 拍照按钮回调
  Function(String path)? clickTakePictureButtonCallback;
  UVCCameraState get getCameraState => _cameraState;
  String _cameraErrorMsg = '';
  String get getCameraErrorMsg => _cameraErrorMsg;
  String _takePicturePath = '';
  String get getTakePicturePath => _takePicturePath;
  final List<String> _callStrings = [];
  List<String> get getCallStrings => _callStrings;
  Function(String)? msgCallback;
  List<PreviewSize> _previewSizes = [];
  List<PreviewSize> get getPreviewSizes => _previewSizes;

  MethodChannel? _cameraChannel;

  UVCCameraController() {
    _cameraChannel = const MethodChannel(_channelName);
    _cameraChannel?.setMethodCallHandler(_methodChannelHandler);
    debugPrint("------> UVCCameraController init");
  }

  void dispose() {
    _cameraChannel?.setMethodCallHandler(null);
    _cameraChannel = null;
    debugPrint("------> UVCCameraController dispose");
  }

  Future<void> _methodChannelHandler(MethodCall call) async {
    switch (call.method) {
      case "callFlutter":
        debugPrint('------> Received from Android：${call.arguments}');
        _callStrings.add(call.arguments.toString());
        msgCallback?.call(call.arguments['msg']);

        break;
      case "takePictureSuccess":
        _takePictureSuccess(call.arguments);
        break;
      case "CameraState":
        _setCameraState(call.arguments.toString());
        break;
      case "onEncodeData":
        final Map<dynamic, dynamic> args = call.arguments;
        // capture H264 & AAC only
        debugPrint(args.toString());
        break;
    }
  }

  Future<void> initializeCamera() async {
    await _cameraChannel?.invokeMethod('initializeCamera');
  }

  Future<void> openUVCCamera() async {
    debugPrint("openUVCCamera");
    await _cameraChannel?.invokeMethod('openUVCCamera');
  }

  // Future<void> writeToDevice(int data) async {
  //   if (_cameraState == UVCCameraState.opened) {
  //     final result = await _cameraChannel?.invokeMethod('writeToDevice', data);
  //     debugPrint(result.toString());
  //   }
  // }

  void captureStreamStart() {
    _cameraChannel?.invokeMethod('captureStreamStart');
  }

  void captureStreamStop() {
    _cameraChannel?.invokeMethod('captureStreamStop');
  }

  void startCamera() async {
    await _cameraChannel?.invokeMethod('startCamera');
  }

  Future getAllPreviewSizes() async {
    var result = await _cameraChannel?.invokeMethod('getAllPreviewSizes');
    List<PreviewSize> list = [];
    json.decode(result)?.forEach((element) {
      list.add(PreviewSize.fromJson(element));
    });
    _previewSizes = list;
  }

  Future<String?> getCurrentCameraRequestParameters() async {
    return await _cameraChannel
        ?.invokeMethod('getCurrentCameraRequestParameters');
  }

  void updateResolution(PreviewSize? previewSize) {
    _cameraChannel?.invokeMethod('updateResolution', previewSize?.toMap());
  }

  Future<String?> takePicture() async {
    String? path = await _cameraChannel?.invokeMethod('takePicture');
    debugPrint("path: $path");
    return path;
  }

  Future<String?> captureVideo() async {
    String? path = await _cameraChannel?.invokeMethod('captureVideo');
    debugPrint("path: $path");
    return path;
  }

  void _setCameraState(String state) {
    debugPrint("Camera: $state");
    switch (state) {
      case "OPENED":
        _cameraState = UVCCameraState.opened;
        cameraStateCallback?.call(UVCCameraState.opened);
        break;
      case "CLOSED":
        _cameraState = UVCCameraState.closed;
        cameraStateCallback?.call(UVCCameraState.closed);
        break;
      default:
        if (state.contains("ERROR")) {
          _cameraState = UVCCameraState.error;
          _cameraErrorMsg = state;
          cameraStateCallback?.call(UVCCameraState.error);
          msgCallback?.call(state);
        }
        break;
    }
  }

  void _takePictureSuccess(String? result) {
    if (result != null) {
      _takePicturePath = result;
      clickTakePictureButtonCallback?.call(result);
    }
  }

  void closeCamera() {
    _cameraChannel?.invokeMethod('closeCamera');
  }
}



class UVCCameraView extends StatefulWidget {
  final UVCCameraController cameraController;
  final double width;
  final double height;
  final UVCCameraViewParamsEntity? params;
  const UVCCameraView(
      {super.key,
      required this.cameraController,
      required this.width,
      required this.height,
      this.params});

  @override
  State<UVCCameraView> createState() => _UVCCameraViewState();
}

class _UVCCameraViewState extends State<UVCCameraView> {
  @override
  void dispose() {
    widget.cameraController.closeCamera();
    widget.cameraController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: widget.width,
      height: widget.height,
      child: AndroidView(
          viewType: 'uvc_camera_view',
          creationParams: widget.params?.toMap(),
          creationParamsCodec: const StandardMessageCodec(),
          onPlatformViewCreated: (id) {
            widget.cameraController.initializeCamera();
          }),
    );
  }
}
