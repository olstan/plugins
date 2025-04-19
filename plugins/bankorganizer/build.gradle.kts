plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("net.runelite.pluginhub.plugin") version "1.4.3"
}

pluginhub {
    pluginId = "bankorganizer"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("net.runelite:client:1.10.20")
}
