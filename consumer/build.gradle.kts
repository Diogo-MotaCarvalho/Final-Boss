plugins {
    id("java")
    id("jacoco")
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

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly ("org.projectlombok:lombok:1.18.34")
    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-mongodb
    implementation ("org.springframework.data:spring-data-mongodb:4.3.4")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.4")
    // https://mvnrepository.com/artifact/com.tngtech.junit.dataprovider/junit-jupiter-dataprovider
    implementation ("com.tngtech.junit.dataprovider:junit-jupiter-dataprovider:2.10")
    testImplementation("org.junit.platform:junit-platform-suite")



    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-suite:latest.release")
    testImplementation("org.junit.jupiter:junit-jupiter-api:latest.release")
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.20.1")
    testImplementation ("org.junit.vintage:junit-vintage-engine:5.7.2")

}
tasks.register<Wrapper>("wrapper") {
    gradleVersion = "8.8"
}
tasks.register("prepareKotlinBuildScriptModel"){}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    // tests are required to run before generating the report
    dependsOn(tasks.test)
    // print the report url for easier access
    doLast {
        println("file://${project.rootDir}/build/reports/jacoco/test/html/index.html")
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/generated/**", "**/other-excluded/**", "**/domain/**","**/config/**","**/FinalClassConsumer")
            }
        })
    )
}


