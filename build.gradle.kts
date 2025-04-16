import com.google.protobuf.gradle.id

plugins {
  java
  id("org.springframework.boot") version "3.4.4"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.protobuf") version "0.9.4"
}

group = "io.pranludi.crossfit"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
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

extra["springGrpcVersion"] = "0.6.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.google.protobuf:protobuf-java:4.28.2")
  implementation("com.google.protobuf:protobuf-java-util:4.28.2")

  implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
  implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")
  implementation("io.grpc:grpc-netty-shaded:1.66.0")
  implementation("io.grpc:grpc-protobuf:1.66.0")
  implementation("io.grpc:grpc-stub:1.66.0")
  implementation("javax.annotation:javax.annotation-api:1.3.2")

  // mapstruct
  implementation("org.mapstruct:mapstruct:1.5.5.Final")
  annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

  // jwt
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

  runtimeOnly("com.h2database:h2")
  runtimeOnly("org.postgresql:postgresql")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:4.28.2"
  }
  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:1.66.0"
    }
  }
  generateProtoTasks {
    all().forEach {
      it.plugins {
        id("grpc") {
          option("jakarta_omit")
          option("@generated=omit")
        }
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
