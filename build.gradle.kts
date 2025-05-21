plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    `maven-publish`
    signing
    java
}

group = "com.koapiclient"
version = "0.0.1-SNAPSHOT"
description = "A kotlin client for the Dify API"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(19))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

val httpclientVersion = "5.4.4"
val jacksonVersion = "2.19.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // HTTP Client
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpclientVersion")


    // JSON 처리
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("Dify API Client")
                description.set("A Java client for the Dify API")
                url.set("https://github.com/skyleedevzero86/ko-api-client")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("skyleedevzero86")
                        name.set("skyleedevzero86")
                        email.set("sleekydz86@naver.com")
                        organization.set("Independent Development")
                        organizationUrl.set("https://github.com/skyleedevzero86")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/skyleedevzero86/ko-api-client.git")
                    developerConnection.set("scm:git:ssh://github.com:skyleedevzero86/ko-api-client.git")
                    url.set("https://github.com/skyleedevzero86/ko-api-client/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            name = "ossrh"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) {
                "https://s01.oss.sonatype.org/content/repositories/snapshots"
            } else {
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            })
            credentials {
                username = findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
