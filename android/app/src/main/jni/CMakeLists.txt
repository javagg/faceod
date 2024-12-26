cmake_minimum_required(VERSION 3.6.4)
project(libuvc VERSION 0.0.7 LANGUAGES C)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_BUILD_TARGET "Shared")

set(LIBJPEG_SRC ${CMAKE_CURRENT_SOURCE_DIR}/src/libjpeg-turbo)

include(ExternalProject)
ExternalProject_Add(JPEG
  SOURCE_DIR ${LIBJPEG_SRC}
  CONFIGURE_COMMAND cmake -DENABLE_STATIC=FALSE -DCMAKE_MAKE_PROGRAM=${CMAKE_MAKE_PROGRAM} -G${CMAKE_GENERATOR} -DANDROID_NDK=${ANDROID_NDK} -DCMAKE_TOOLCHAIN_FILE=${CMAKE_TOOLCHAIN_FILE} -DANDROID_PLATFORM=${ANDROID_PLATFORM} ${LIBJPEG_SRC}
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

set(LIBUVC_ROOT src/libuvc)
set(LIBUVC_SRCS
  ${LIBUVC_ROOT}/src/ctrl.c
  ${LIBUVC_ROOT}/src/ctrl-gen.c
  ${LIBUVC_ROOT}/src/device.c
  ${LIBUVC_ROOT}/src/diag.c
  ${LIBUVC_ROOT}/src/frame.c
  # ${LIBUVC_ROOT}/src/frame-mjpeg.c
  ${LIBUVC_ROOT}/src/init.c
  ${LIBUVC_ROOT}/src/stream.c
  ${LIBUVC_ROOT}/src/misc.c
)

set(JNI_SRCS generated/jnijavacpp.cpp generated/jniuvc.cpp)

ExternalProject_Get_property(JPEG SOURCE_DIR)
ExternalProject_Get_property(JPEG BINARY_DIR)
link_directories(${BINARY_DIR})
include_directories(include src/libusb/libusb/android src/libusb/libusb src/libuvc/include ${SOURCE_DIR} ${BINARY_DIR})
add_library(uvc SHARED ${LIBUVC_SRCS} ${LIBUSB_SRCS})
add_dependencies(uvc JPEG)
target_link_libraries(
  uvc
  log
  # jpeg
)

#find_package(SWIG)
#include(UseSWIG)
#set(CMAKE_SWIG_FLAGS -package org.faceod.camera)
#swig_add_library(camera
#  LANGUAGE java
#  SOURCES libuvc.i
#  OUTPUT_DIR ${CMAKE_CURRENT_SOURCE_DIR}/generated/org/faceod/camera
#)
add_library(camera SHARED ${JNI_SRCS})
set_target_properties(camera PROPERTIES LINKER_LANGUAGE CXX)
target_link_libraries(camera uvc)