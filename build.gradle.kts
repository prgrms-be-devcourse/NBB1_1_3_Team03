plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.10"
}

val jacocoDir = layout.buildDirectory.dir("reports/jacoco")

tasks.withType<JacocoReport> {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(jacocoDir.get().dir("html"))
        xml.outputLocation.set(jacocoDir.get().file("jacoco.xml"))
    }
}

allOpen {
    annotation("org.springframework.stereotype.Service")
    annotation("org.springframework.web.bind.annotation.RestController")
    annotation("org.springframework.stereotype.Component")
    annotation("org.springframework.transaction.annotation.Transactional")
}

sonar {
    properties {
        property("sonar.projectKey", "prgrms-be-devcourse_NBE1_2_Team03")
        property("sonar.organization", "prgrms-be-devcourse")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

group = "com.sscanner"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // 스케줄러
    implementation("org.springframework:spring-context-support")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // S3 설정 추가
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    // JWT 의존성 추가
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // QR 코드 생성용 ZXing
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.zxing:javase:3.4.1")

    // Nurigo 문자 인증
    implementation("net.nurigo:sdk:4.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // 모니터링
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation ("io.micrometer:micrometer-registry-prometheus")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.named<JacocoReport>("jacocoTestReport"))
}
