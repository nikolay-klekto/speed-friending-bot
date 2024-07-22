//import nu.studer.gradle.jooq.JooqEdition
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//
//plugins {
//    id("org.springframework.boot") version "2.7.4"
//    id("io.spring.dependency-management") version "1.0.14.RELEASE"
//    kotlin("jvm") version "1.9.23"
//    kotlin("plugin.spring") version "1.8.21"
//    id("nu.studer.jooq") version "7.1.1"
//
//}
//
//group = "by.sf.boy"
//version = "1.0-SNAPSHOT"
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation("org.jooq:jooq")
//    implementation("org.jooq:jooq-meta")
//    implementation("org.jooq:jooq-codegen")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // для Jooq
//    implementation("org.springframework.boot:spring-boot-starter-jooq")
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    implementation("org.postgresql:r2dbc-postgresql:1.0.5.RELEASE")
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("org.jooq:jooq")
//    implementation("org.jooq:jooq-kotlin")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("io.projectreactor:reactor-test")
//    implementation("org.telegram:telegrambots-spring-boot-starter:5.6.0")
//    runtimeOnly("org.postgresql:postgresql")
//    testImplementation("org.springframework.graphql:spring-graphql-test")
//    implementation("org.springframework.boot:spring-boot-starter-cache")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//    implementation("org.springframework.boot:spring-boot-starter-graphql")
//    implementation("org.springframework.data:spring-data-r2dbc")
//    implementation("org.springframework.data:spring-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//
//    jooqGenerator("org.glassfish.jaxb:jaxb-runtime:2.3.3")
//    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
//    jooqGenerator("org.postgresql:postgresql:42.5.0")
//
//
//}
//
//tasks.test {
//    useJUnitPlatform()
//}
//
//tasks.withType<KotlinCompile> {
//    kotlinOptions {
//        freeCompilerArgs = listOf("-Xjsr305=strict")
//        jvmTarget = "17"
//    }
//}
//
//kotlin {
//    jvmToolchain(17)
//}
//
//
//java.sourceSets["main"].java {
//    srcDir("src/generated/jooq")
//}
//
//jooq {
//    version.set("3.16.7")
//    edition.set(JooqEdition.OSS)
//
//    configurations {
//        create("main") {
//            jooqConfiguration.apply {
//                jdbc.apply {
//                    driver = "org.postgresql.Driver"
//                    url = "jdbc:postgresql://45.135.234.61:15432/speed_friending"
//                    user = "username"
//                    password = "password"
//                }
//                generator.apply {
//                    name = "org.jooq.codegen.KotlinGenerator"
//                    database.apply {
//                        name = "org.jooq.meta.postgres.PostgresDatabase"
//                        inputSchema = "public"
//
//                    }
//                    generate.apply {
//                        isDeprecated = false
//                        isRecords = true
//                        isImmutablePojos = false
//                        isFluentSetters = true
//                        isPojos = true
//                        withSequences(false)
//                    }
//                    target.apply {
//                        packageName = "by.sf.bot.jooq"
//                        directory = "src/generated/jooq"
//                    }
//                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
//                }
//            }
//        }
//    }
//}

///////////////////////////////////////////////////////////

import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("nu.studer.jooq") version "7.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.jooq:jooq")
    runtimeOnly("org.postgresql:postgresql")
    jooqGenerator("org.postgresql:postgresql:42.3.5")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.telegram:telegrambots-spring-boot-starter:5.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("speedFriendingBot-${version}.jar")
}

//java.sourceSets["main"].java {
//    srcDir("src/generated/jooq")
//}

jooq {
    version.set("3.16.7")
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://45.135.234.61:15432/speed_friending"
                    user = "username"
                    password = "password"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"

                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = true
                        isPojos = true
                        withSequences(false)
                    }
                    target.apply {
                        packageName = "by.sf.bot.jooq"
                        directory = "src/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}