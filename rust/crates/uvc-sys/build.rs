extern crate bindgen;

use std::env;
use std::path::PathBuf;

fn main() {
    let uvc_dir = std::env::var("DEPS_UVC_DIR").unwrap();
    let os = std::env::var("CARGO_CFG_TARGET_OS").unwrap();
    let arch = match std::env::var("CARGO_CFG_TARGET_ARCH").unwrap().as_str() {
        // "arm" => "armeabi-v7a",
        "aarch64" => "arm64-v8a",
        "x86_64" => "x86_64",
        _ => panic!("not supported")
    };

    let includedir = format!("{}/{}/{}/include", uvc_dir, os, arch);
    println!("{}", includedir);
    let builder = bindgen::Builder::default();
    let bindings = builder
        .clang_arg(format!("-I{}", includedir))
        .header("wrapper.h")
        .allowlist_function("uvc_.*")
        .allowlist_type("uvc_.*")
        .generate()
        .expect("Failed to generate bindings");

    let out_path = PathBuf::from(env::var("OUT_DIR").unwrap());
    bindings
        .write_to_file(out_path.join("uvc_bindings.rs"))
        .expect("Failed to write bindings");
}