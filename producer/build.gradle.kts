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
    // https://mvnrepository.com/artifact/io.cucumber/cucumber-java
    implementation ("io.cucumber:cucumber-java:2.0.0")


    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly ("org.projectlombok:lombok:1.18.34")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
                exclude("**/generated/**", "**/other-excluded/**", "**/domain/**","**/config/**","com.finalboss.FinalClassProducer")
            }
        })
    )
}


