cmake_minimum_required(VERSION 3.6.4)
project(libuvc VERSION 0.0.7 LANGUAGES C)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_BUILD_TARGET "Shared")

include(ExternalProject)
ExternalProject_Add(JPEG
  SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/libjpeg-turbo
  CONFIGURE_COMMAND cmake -DCMAKE_MAKE_PROGRAM=${CMAKE_MAKE_PROGRAM} -G${CMAKE_GENERATOR} -DANDROID_NDK=${ANDROID_NDK} -DCMAKE_TOOLCHAIN_FILE=${CMAKE_TOOLCHAIN_FILE} -DANDROID_PLATFORM=${ANDROID_PLATFORM} ${CMAKE_CURRENT_SOURCE_DIR}/src/libjpeg-turbo
  BUILD_COMMAND ${CMAKE_MAKE_PROGRAM}
  INSTALL_COMMAND ""
)

set(LIBUSB_ROOT src/libusb)
set(LIBUSB_SRCS
  ${LIBUSB_ROOT}/libusb/core.c
  ${LIBUSB_ROOT}/libusb/descriptor.c
  ${LIBUSB_ROOT}/libusb/hotplug.c
  ${LIBUSB_ROOT}/libusb/io.c
  ${LIBUSB_ROOT}/libusb/sync.c
  ${LIBUSB_ROOT}/libusb/strerror.c
  ${LIBUSB_ROOT}/libusb/os/linux_usbfs.c
  ${LIBUSB_ROOT}/libusb/os/events_posix.c
  ${LIBUSB_ROOT}/libusb/os/threads_posix.c
  ${LIBUSB_ROOT}/libusb/os/linux_netlink.c
)


set(LIBJPEG_ROOT src/libjpeg-turbo)
set(LIBJPEG_SRCS
  ${LIBUSB_ROOT}/libusb/core.c
  ${LIBUSB_ROOT}/libusb/descriptor.c
  ${LIBUSB_ROOT}/libusb/hotplug.c
  ${LIBUSB_ROOT}/libusb/io.c
  ${LIBUSB_ROOT}/libusb/sync.c
  ${LIBUSB_ROOT}/libusb/strerror.c
  ${LIBUSB_ROOT}/libusb/os/linux_usbfs.c
  ${LIBUSB_ROOT}/libusb/os/events_posix.c
  ${LIBUSB_ROOT}/libusb/os/threads_posix.c
  ${LIBUSB_ROOT}/libusb/os/linux_netlink.c
)

set(LIBUVC_ROOT src/libuvc)
set(LIBUVC_SRCS
  ${LIBUVC_ROOT}/src/ctrl.c
  ${LIBUVC_ROOT}/src/ctrl-gen.c
  ${LIBUVC_ROOT}/src/device.c
  ${LIBUVC_ROOT}/src/diag.c
  ${LIBUVC_ROOT}/src/frame.c
  ${LIBUVC_ROOT}/src/init.c
  ${LIBUVC_ROOT}/src/stream.c
  ${LIBUVC_ROOT}/src/misc.c
)

set(SRCS ${LIBUVC_SRCS} ${LIBUSB_SRCS} ${LIBJPEG_SRCS})
add_library(uvc SHARED ${SRCS})

add_dependencies(uvc JPEG)
# set_target_properties(uvc PROPERTIES
# VERSION ${libuvc_VERSION}
# SOVERSION ${libuvc_VERSION_MAJOR}
# # Exported name of target within namespace LibUVC
# EXPORT_NAME UVCShared
# )

include_directories(include src/libusb/libusb/android src/libusb/libusb src/libuvc/include)
target_link_libraries(uvc log)

# set(CMAKE_SWIG_FLAGS "")
include(UseSWIG)
swig_add_library(camera
  LANGUAGE java
  SOURCES libuvc.i
)

# set_property(TARGET myLibrary PROPERTY SWIG_COMPILE_OPTIONS -package com.foo.bar.baz)
