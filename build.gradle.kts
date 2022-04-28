import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.4.31"
}

/*sun.tools.jar.resources.jar {
    manifest {
        attributes(
            'Main-Class': 'it.polito.wa2project.wa2projectcatalogservice.Wa2ProjectCatalogServiceApplication.kt'
        )
    }
}*/

allOpen{
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "it.polito.wa2project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2020.0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // VALIDATION
    implementation("org.hibernate.validator:hibernate-validator")
    implementation("javax.validation:validation-api")

    // SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("org.projectlombok:lombok:1.18.20")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // DB DRIVER
    runtimeOnly("mysql:mysql-connector-java")

    // KAFKA
    implementation("org.springframework.kafka:spring-kafka:2.7.8")

    // DEBEZIUM
    implementation("io.debezium:debezium-api:1.7.1.Final")
    implementation("io.debezium:debezium-embedded:1.7.1.Final")
    implementation("io.debezium:debezium-connector-mysql:1.7.0.Final")

    // EUREKA CLIENT
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // EXTRA
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
