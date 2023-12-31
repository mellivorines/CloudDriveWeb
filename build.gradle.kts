import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
    kotlin("jvm") version "1.8.21"
    kotlin("kapt") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
}

group = "io.github.mellivorines"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

val jimmerVersion = "0.7.98"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
    kapt("org.babyfish.jimmer:jimmer-mapstruct-apt:${jimmerVersion}")

    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0")

    implementation("com.github.tobato:fastdfs-client:1.27.2")
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.17.0")

//    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt:0.9.1")


    implementation("com.google.guava:guava:32.0.1-jre")

    implementation("org.reflections:reflections:0.10.2")

    runtimeOnly("io.lettuce:lettuce-core:6.2.0.RELEASE")

    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}