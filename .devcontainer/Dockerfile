# /////syntax=docker/dockerfile:1
FROM ghcr.io/cirruslabs/flutter:stable

ENV ANDROID_NDK_VERSION=26.2.11394342

RUN yes | sdkmanager "ndk;$ANDROID_NDK_VERSION"

ENV NDK=/opt/android-sdk-linux/ndk/$ANDROID_NDK_VERSION
ENV PATH=$NDK:$PATH
ENV MINSDKVER=21
ENV LANG=C.UTF-8

# This Dockerfile adds a non-root user with sudo access. Use the "remoteUser"
# property in devcontainer.json to use it. On Linux, the container user's GID/UIDs
# will be updated to match your local UID/GID (when using the dockerFile property).
# See https://aka.ms/vscode-remote/containers/non-root-user for details.
# ARG USERNAME=vscode
# ARG USER_UID=1000
# ARG USER_GID=$USER_UID

# Install needed packages, setup user anda clean up.
RUN  apt update \
	&& apt install -y cmake nasm clang gcc-multilib --no-install-recommends

	# Clean Up
RUN	apt-get autoremove -y \
	&& apt-get clean -y \
	&& rm -rf /var/lib/apt/lists/* 
	# Create a non-root user to use if preferred - see https://aka.ms/vscode-remote/containers/non-root-user.
	# [Optional] Add sudo support for the non-root user

# install dep libs for android
WORKDIR /deps
RUN git clone --depth 1 -b v1.0.27 https://github.com/libusb/libusb.git
RUN git clone --depth 1 -b 3.0.4 https://github.com/libjpeg-turbo/libjpeg-turbo.git
RUN git clone --depth 1 -b v0.0.7 https://github.com/libuvc/libuvc.git

ENV DEPS=/deps/lib

RUN cd libusb/android/jni && ndk-build \
  && mkdir -p /deps/lib/libusb/android \
  && cd /deps/lib/libusb/android && mkdir arm64-v8a && mkdir x86_64 && cd arm64-v8a && mkdir include && mkdir lib \
  && cd .. && cd x86_64 && mkdir include && mkdir lib \
  && cp -R /deps/libusb/android/libs/arm64-v8a/* /deps/lib/libusb/android/arm64-v8a/lib \
  && cp -R /deps/libusb/android/libs/x86_64/* /deps/lib/libusb/android/x86_64/lib \
  && cp -R /deps/libusb/libusb/libusb.h /deps/lib/libusb/android/arm64-v8a/include \
  && cp -R /deps/libusb/libusb/libusb.h /deps/lib/libusb/android/x86_64/include

RUN cd libjpeg-turbo \
  && export LIBJPEG_BUILD_ARGS="-DANDROID_NDK=$NDK -DCMAKE_TOOLCHAIN_FILE=$NDK/build/cmake/android.toolchain.cmake -DANDROID_PLATFORM=android-$MINSDKVER -DCMAKE_BUILD_TYPE=Release" \
  && mkdir build && cd build && cmake $LIBJPEG_BUILD_ARGS -DANDROID_ABI=arm64-v8a -DCMAKE_INSTALL_PREFIX=/deps/lib/libjpeg/android/arm64-v8a -DCMAKE_ASM_FLAGS="--target=aarch64-linux-android$MINSDKVER" .. \
  && make && make install && cd .. && rm -rf build \
  && mkdir build && cd build && cmake $LIBJPEG_BUILD_ARGS -DANDROID_ABI=x86_64 -DCMAKE_INSTALL_PREFIX=/deps/lib/libjpeg/android/x86_64 .. \
  && make && make install && cd .. && rm -rf build

RUN export LIBUVC_BUILD_ARGS="-DANDROID_NDK=$NDK -DCMAKE_TOOLCHAIN_FILE=$NDK/build/cmake/android.toolchain.cmake -DANDROID_PLATFORM=android-$MINSDKVER -DCMAKE_BUILD_TYPE=Release" 

COPY FindJpegPkg.cmake FindLibUSB.cmake /deps/libuvc/cmake/
RUN cd libuvc \
  && export LIBUVC_BUILD_ARGS="-DANDROID_NDK=$NDK -DCMAKE_TOOLCHAIN_FILE=$NDK/build/cmake/android.toolchain.cmake -DANDROID_PLATFORM=android-$MINSDKVER -DCMAKE_BUILD_TYPE=Release" \
  && mkdir build && cd build && cmake $LIBUVC_BUILD_ARGS -DANDROID_ABI=arm64-v8a -DCMAKE_INSTALL_PREFIX=/deps/lib/libuvc/android/arm64-v8a .. \
  && make && make install && cd .. && rm -rf build \
  && mkdir build && cd build && cmake $LIBUVC_BUILD_ARGS -DANDROID_ABI=x86_64 -DCMAKE_INSTALL_PREFIX=/deps/lib/libuvc/android/x86_64 .. \
  && make && make install && cd .. && rm -rf build

# Install Rust for the correct CPU architecture
ENV RUST_VERSION=1.82.0
ENV RUSTUP_INIT_VERSION=1.27.1
ENV RUSTUP_HOME=/usr/local/rustup \
    CARGO_HOME=/usr/local/cargo \
    PATH=/usr/local/cargo/bin:$PATH
    
RUN arch="$(uname -m)" && \
    case "$arch" in \
      x86_64) rustArch='x86_64-unknown-linux-gnu'; rustupSha256='6aeece6993e902708983b209d04c0d1dbb14ebb405ddb87def578d41f920f56d' ;; \
      aarch64) rustArch='aarch64-unknown-linux-gnu'; rustupSha256='1cffbf51e63e634c746f741de50649bbbcbd9dbe1de363c9ecef64e278dba2b2' ;; \
      *) echo >&2 "unsupported architecture: ${dpkgArch}"; exit 1 ;; \
    esac && \
    wget -qO /tmp/rustup-init "https://static.rust-lang.org/rustup/archive/${RUSTUP_INIT_VERSION}/${rustArch}/rustup-init" && \
    echo "${rustupSha256}  /tmp/rustup-init" | sha256sum -c - && \
    chmod +x /tmp/rustup-init && \
    /tmp/rustup-init -y --no-modify-path --profile minimal --default-toolchain ${RUST_VERSION} --default-host ${rustArch} && \
    rm /tmp/rustup-init && \
    chmod -R a+w ${RUSTUP_HOME} ${CARGO_HOME} && \
    rustup --version && cargo --version && rustc --version

# # Install:
# # - gcc, libc6-dev required by Rust
# # - musl-tools required for static binaries
# RUN export DEBIAN_FRONTEND=noninteractive && \
#     apt-get update && \
#     apt-get -y install --no-install-recommends gcc libc6-dev musl-tools && \
#     apt-get clean && \
#     rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# # Install Rust tooling
# ARG RUST_ANALYZER_VERSION=2024-02-05
# RUN wget -qO- "https://github.com/rust-analyzer/rust-analyzer/releases/download/${RUST_ANALYZER_VERSION}/rust-analyzer-$(uname -m)-unknown-linux-gnu.gz" | \
#     gunzip > /usr/local/bin/rust-analyzer && \
#     chmod 500 /usr/local/bin/rust-analyzer
RUN rustup component add clippy rustfmt && \
    rustup target add aarch64-linux-android armv7-linux-androideabi x86_64-linux-android i686-linux-android && \
    cargo install cargo-binstall cargo-ndk flutter_rust_bridge_codegen