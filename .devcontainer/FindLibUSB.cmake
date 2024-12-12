set(LibUSB_INCLUDE_DIR /deps/lib/libusb/android/${ANDROID_ABI}/include)
set(LibUSB_LIBRARY /deps/lib/libusb/android/${ANDROID_ABI}/lib/libusb1.0.so)

add_library(LibUSB::LibUSB
  UNKNOWN IMPORTED
)

set_target_properties(LibUSB::LibUSB PROPERTIES
  INTERFACE_INCLUDE_DIRECTORIES ${LibUSB_INCLUDE_DIR}
)

set_target_properties(LibUSB::LibUSB PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES "C"
  IMPORTED_LOCATION ${LibUSB_LIBRARY}
)

if (LibUSB_INCLUDE_DIR AND LibUSB_LIBRARY)
	set(LibUSB_FOUND TRUE)
endif (LibUSB_INCLUDE_DIR AND LibUSB_LIBRARY)