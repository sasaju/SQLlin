plugins {
    kotlin("jvm")
    id("maven-publish")
    signing
}

val GROUP: String by project
val VERSION: String by project

group = GROUP
version = VERSION

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val kspVersion: String by project
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}

val NEXUS_USERNAME: String by project
val NEXUS_PASSWORD: String by project

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.create<MavenPublication>("Processor") {
        artifactId = "sqllin-processor"
        setArtifacts(listOf("$buildDir/libs/sqllin-processor-$version.jar", javadocJar))
        with(pom) {
            name.set("sqllin-processor")
            description.set("KSP code be used to generate the database column properties")
            url.set("https://github.com/ctripcorp/SQLlin")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("qiaoyuang")
                    name.set("Yuang Qiao")
                    email.set("qiaoyuang2012@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/ctripcorp/SQLlin")
                connection.set("scm:git:https://github.com/ctripcorp/SQLlin.git")
                developerConnection.set("scm:git:https://github.com/ctripcorp/SQLlin.git")
            }
        }
    }
    repositories {
        maven {
            credentials {
                username = NEXUS_USERNAME
                password = NEXUS_PASSWORD
            }
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
        }
    }
    signing {
        sign(publishing.publications)
    }
}