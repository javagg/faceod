import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

allprojects {
    repositories {
//        maven { url = java.net.URI("https://maven.aliyun.com/repository/google") }
//        maven { url = java.net.URI("https://maven.aliyun.com/repository/jcenter") }
//        maven { url = java.net.URI("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
    }
}

val rootBuildDir = Path("${rootProject.rootDir}").resolve("../build").absolutePathString()
rootProject.layout.buildDirectory.fileValue(File(rootBuildDir))
subprojects {
    project.layout.buildDirectory.fileValue(Path(rootBuildDir, project.name).toFile())
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}