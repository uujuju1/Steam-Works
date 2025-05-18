pluginManagement{
    repositories{
        gradlePluginPortal()
    }
}

if(JavaVersion.current().ordinal < JavaVersion.VERSION_17.ordinal){
    throw IllegalStateException("JDK 17 is a required minimum version. Yours: ${System.getProperty("java.version")}")
}

include(":tools")

val modName: String by settings
rootProject.name = modName
