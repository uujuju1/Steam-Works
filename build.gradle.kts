import arc.files.*
import arc.util.*
import arc.util.serialization.*
import ent.*
import java.io.*
import java.util.regex.*

buildscript{
    val arcVersion: String by project
    val useJitpack = property("mindustryBE").toString().toBooleanStrict()

    dependencies{
        classpath("com.github.Anuken.Arc:arc-core:$arcVersion")
    }

    repositories{
        if(!useJitpack) maven("https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository")
        maven("https://jitpack.io")
    }
}

plugins{
    java
    id("com.github.GlennFolker.EntityAnno") apply false
}

val arcVersion: String by project
val mindustryVersion: String by project
val mindustryBEVersion: String by project
val entVersion: String by project

val modName: String by project
val modArtifact: String by project
val modFetch: String by project
val modGenSrc: String by project
val modGen: String by project

val androidSdkVersion: String by project
val androidBuildVersion: String by project
val androidMinVersion: String by project

val useJitpack = property("mindustryBE").toString().toBooleanStrict()

fun arc(module: String): String{
    return "com.github.Anuken.Arc$module:$arcVersion"
}

fun mindustry(module: String): String{
    return "com.github.Anuken.Mindustry$module:$mindustryVersion"
}

fun entity(module: String): String{
    return "com.github.GlennFolker.EntityAnno$module:$entVersion"
}

allprojects{
    apply(plugin = "java")
    sourceSets["main"].java.setSrcDirs(listOf(layout.projectDirectory.dir("src")))

    configurations.configureEach{
        // Resolve the correct Mindustry dependency, and force Arc version.
        resolutionStrategy.eachDependency{
            if(useJitpack && requested.group == "com.github.Anuken.Mindustry"){
                useTarget("com.github.Anuken.MindustryJitpack:${requested.module.name}:$mindustryBEVersion")
            }else if(requested.group == "com.github.Anuken.Arc"){
                useVersion(arcVersion)
            }
        }
    }

    dependencies{
        // Downgrade Java 9+ syntax into being available in Java 8.
        annotationProcessor(entity(":downgrader"))
    }

    repositories{
        // Necessary Maven repositories to pull dependencies from.
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://raw.githubusercontent.com/GlennFolker/EntityAnnoMaven/main")

        // Use Zelaux's non-buggy repository for release Mindustry and Arc builds.
        if(!useJitpack) maven("https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository")
        maven("https://jitpack.io")
    }

    tasks.withType<JavaCompile>().configureEach{
        // Use Java 17+ syntax, but target Java 8 bytecode version.
        sourceCompatibility = "17"
        options.apply{
            release = 8
            compilerArgs.add("-Xlint:-options")

            isIncremental = true
            encoding = "UTF-8"
        }
    }
}

project(":tools") {
    dependencies{
        implementation(rootProject)

        implementation(mindustry(":core"))
        implementation(arc(":arc-core"))
        implementation(arc(":natives-desktop"))
    }

    tasks.register<JavaExec>("run"){
        val assets = rootProject.layout.projectDirectory.dir("assets")
        val out = assets.dir("sprites")
        val raw = assets.dir("sprites-raw")

        inputs.files(raw)
        outputs.dir(out)

        mainClass = "sw.tools.Tools"
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = temporaryDir
        standardInput = System.`in`
        args(assets.asFile)

        doFirst{
            val dir = out.asFile
            dir.deleteRecursively()
            dir.mkdirs()

            temporaryDir.deleteRecursively()
            temporaryDir.mkdirs()

            copy {
                from(files(raw))
                into(File(temporaryDir, "sprites"))
            }
        }

        doLast{
            copy{
                from(files(File(temporaryDir, "sprites")))
                into(out.asFile)
            }
        }
    }
}

project(":"){
    apply(plugin = "com.github.GlennFolker.EntityAnno")
    configure<EntityAnnoExtension>{
        modName = project.properties["modName"].toString()
        mindustryVersion = project.properties[if(useJitpack) "mindustryBEVersion" else "mindustryVersion"].toString()
        isJitpack = useJitpack
        revisionDir = layout.projectDirectory.dir("revisions").asFile
        fetchPackage = modFetch
        genSrcPackage = modGenSrc
        genPackage = modGen
    }

    dependencies{
        // Use the entity generation annotation processor.
        compileOnly(entity(":entity"))
        add("kapt", entity(":entity"))

        compileOnly(mindustry(":core"))
        compileOnly(arc(":arc-core"))
    }

    val list = tasks.register<DefaultTask>("list") {
        inputs.files(tasks.named<JavaCompile>("compileJava"), configurations.runtimeClasspath)

        val output = layout.projectDirectory.dir("assets").dir("meta").dir("sw").file("classes.json").asFile
        outputs.file(output)

        doFirst{
            output.parentFile.mkdirs()
            val packages = Jval.newArray()
            val classes = Jval.newArray()

            val forbid = Pattern.compile("\\$\\d+|.+Impl")
            fun proc(path: String, dir: File){
                dir.listFiles()?.forEach{
                    if(it.isDirectory && (path.startsWith("sw") || it.name == "sw")){
                        val visited = if(path.isEmpty()) it.name else "$path.${it.name}"
                        if(visited != modFetch && visited != modGenSrc){
                            packages.add(visited)
                            proc(visited, it)
                        }
                    }else{
                        val dot = it.name.lastIndexOf('.')
                        if(dot != -1){
                            val name = it.name.substring(0, dot)
                            val ext = it.name.substring(dot + 1)

                            if(ext == "class" && !forbid.matcher(name).matches()) classes.add("$path.$name")
                        }
                    }
                }
            }

            sourceSets["main"].runtimeClasspath.forEach{
                if(it.isDirectory){
                    proc("", it)
                }else if(it.exists()){
                    zipTree(it).forEach{inner -> proc("", inner)}
                }
            }

            val compacted = Jval.newObject().put("packages", packages).put("classes", classes)
            BufferedWriter(FileWriter(output, Charsets.UTF_8, false)).use{compacted.writeTo(it, Jval.Jformat.formatted)}
        }
    }

    tasks.named<Jar>("jar"){
        inputs.files(list)
        archiveFileName = "base.jar"
    }

    val dep = tasks.register<Jar>("dep"){
        val proc = project(":tools").tasks.named<JavaExec>("run")

        mustRunAfter(proc)

        if(!layout.projectDirectory.dir("assets").dir("sprites").asFile.exists()){
            logger.lifecycle("Sprites folder not found; automatically running `:tools:run`.")
            inputs.files(proc)
        }

        archiveFileName = "${modArtifact}Desktop.jar"

        val meta = layout.projectDirectory.file("$temporaryDir/mod.json")
        from(
            files(sourceSets["main"].output.classesDirs),
            files(sourceSets["main"].output.resourcesDir),
            configurations.runtimeClasspath.map{conf -> conf.map{if(it.isDirectory) it else zipTree(it)}},

            layout.projectDirectory.file("icon.png"),
            meta
        )
        from(files(layout.projectDirectory.dir("assets"))).exclude("sprites-raw")

        metaInf.from(layout.projectDirectory.file("LICENSE"))
        doFirst{
            // Deliberately check if the mod meta is actually written in HJSON, since, well, some people actually use
            // it. But this is also not mentioned in the `README.md`, for the mischievous reason of driving beginners
            // into using JSON instead.
            val metaJson = layout.projectDirectory.file("mod.json")
            val metaHjson = layout.projectDirectory.file("mod.hjson")

            if(metaJson.asFile.exists() && metaHjson.asFile.exists()){
                throw IllegalStateException("Ambiguous mod meta: both `mod.json` and `mod.hjson` exist.")
            }else if(!metaJson.asFile.exists() && !metaHjson.asFile.exists()){
                throw IllegalStateException("Missing mod meta: neither `mod.json` nor `mod.hjson` exist.")
            }

            val isJson = metaJson.asFile.exists()
            val map = (if(isJson) metaJson else metaHjson).asFile
                .reader(Charsets.UTF_8)
                .use{Jval.read(it)}

            map.put("name", modName)
            meta.asFile.writer(Charsets.UTF_8).use{file -> BufferedWriter(file).use{map.writeTo(it, Jval.Jformat.formatted)}}
        }
    }

    val dex = tasks.register<Jar>("dex"){
        inputs.files(dep)
        archiveFileName = "$modArtifact.jar"

        val desktopJar = dep.flatMap{it.archiveFile}
        val dexJar = File(temporaryDir, "Dex.jar")

        from(zipTree(desktopJar), zipTree(dexJar))
        doFirst{
            logger.lifecycle("Running `d8`.")
            providers.exec{
                // Find Android SDK root.
                val sdkRoot = File(
                    OS.env("ANDROID_SDK_ROOT") ?: OS.env("ANDROID_HOME") ?:
                    throw IllegalStateException("Neither `ANDROID_SDK_ROOT` nor `ANDROID_HOME` is set.")
                )
    
                // Find `d8`.
                val d8 = File(sdkRoot, "build-tools/$androidBuildVersion/${if(OS.isWindows) "d8.bat" else "d8"}")
                if(!d8.exists()) throw IllegalStateException("Android SDK `build-tools;$androidBuildVersion` isn't installed or is corrupted")
    
                // Initialize a release build.
                val input = desktopJar.get().asFile
                val command = arrayListOf("$d8", "--release", "--min-api", androidMinVersion, "--output", "$dexJar", "$input")
    
                // Include all compile and runtime classpath.
                (configurations.compileClasspath.get().toList() + configurations.runtimeClasspath.get().toList()).forEach{
                    if(it.exists()) command.addAll(arrayOf("--classpath", it.path))
                }
    
                // Include Android platform as library.
                val androidJar = File(sdkRoot, "platforms/android-$androidSdkVersion/android.jar")
                if(!androidJar.exists()) throw IllegalStateException("Android SDK `platforms;android-$androidSdkVersion` isn't installed or is corrupted")
    
                command.addAll(arrayOf("--lib", "$androidJar"))
                if(OS.isWindows) command.addAll(0, arrayOf("cmd", "/c").toList())
    
                // Run `d8`.
                commandLine(command)
            }.result.get().rethrowFailure()
        }
    }

    tasks.register<DefaultTask>("install"){
        inputs.files(dep)

        val desktopJar = dep.flatMap{it.archiveFile}
        val dexJar = dex.flatMap{it.archiveFileName}
        doLast{
            val folder = Fi.get(OS.getAppDataDirectoryString("Mindustry")).child("mods")
            folder.mkdirs()

            val input = desktopJar.get().asFile
            folder.child(input.name).delete()
            folder.child(dexJar.get()).delete()
            Fi(input).copyTo(folder)

            logger.lifecycle("Copied :jar output to $folder.")
        }
    }
}
