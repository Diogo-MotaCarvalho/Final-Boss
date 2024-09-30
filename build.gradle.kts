
plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
    implementation ("org.apache.kafka:kafka-clients:3.8.0")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation ("org.slf4j:slf4j-api:2.0.16")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation ("org.springframework.boot:spring-boot-starter-web:3.3.3")

    // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
    implementation ("org.springframework.kafka:spring-kafka:3.2.4")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation ("org.springframework.boot:spring-boot-starter-test:3.3.4")

    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-mongodb
    implementation ("org.springframework.data:spring-data-mongodb:4.3.4")


    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly ("org.projectlombok:lombok:1.18.34")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}