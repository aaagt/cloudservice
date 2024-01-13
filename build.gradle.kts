plugins {
    id("java")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "aaagt.cloudservice"
    version = "1.0.0"

    apply {
        plugin("java")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile>().configureEach {
        //options.compilerArgs.add("--enable-preview")
        javaCompiler.set(javaToolchains.compilerFor {
            languageVersion.set(JavaLanguageVersion.of(17))
        })
    }
}
