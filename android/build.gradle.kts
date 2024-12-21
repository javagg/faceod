allprojects {
    repositories {
        maven { url = java.net.URI("https://maven.aliyun.com/repository/google") }
        maven { url = java.net.URI("https://maven.aliyun.com/repository/jcenter") }
        maven { url = java.net.URI("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
    }
}

rootProject.buildDir = File("../build")
subprojects {
    project.buildDir =File("${rootProject.buildDir}/${project.name}")
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}
//tasks.register("clean") {
//    delete(rootProject.buildDir)
//}