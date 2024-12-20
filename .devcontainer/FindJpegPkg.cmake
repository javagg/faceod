set(JPEG_INCLUDE_DIR /deps/lib/libjpeg/android/${ANDROID_ABI}/include)
set(JPEG_LIBRARY /deps/lib/libjpeg/android/${ANDROID_ABI}/lib/libjpeg.so)

add_library(JPEG::JPEG
  UNKNOWN IMPORTED
)

set_target_properties(JPEG::JPEG PROPERTIES
  INTERFACE_INCLUDE_DIRECTORIES ${JPEG_INCLUDE_DIR}
)

set_target_properties(JPEG::JPEG PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES "C"
  IMPORTED_LOCATION ${JPEG_LIBRARY}
)

if(JPEG_INCLUDE_DIR AND JPEG_LIBRARY)
	set(JPEG_FOUND TRUE)
endif()