plugins {
    id("io.qameta.allure") version "2.11.2"
    id("java")
}

group = "org.testTask"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.testng:testng:7.9.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("io.rest-assured:rest-assured:5.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("org.assertj:assertj-core:3.24.2")
    implementation("com.opencsv:opencsv:5.9")
    testImplementation("io.qameta.allure:allure-testng:2.24.0")
    testImplementation("org.aspectj:aspectjweaver:1.9.20")

}

val cleanAllure by tasks.registering(Delete::class) {
    delete("build/allure-results", "build/allure-report")
}

tasks.register("showReport") {
    dependsOn("allureReport")
    finalizedBy("allureServe")
}

tasks.test {
    dependsOn(cleanAllure)
    useTestNG {
        testLogging {
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }

        val uriParam = project.findProperty("uri") as? String ?: "http://3.68.165.45/"
        systemProperty("uri", uriParam)


        val parallelMode = project.findProperty("parallel") as? String ?: "methods"
        val threads = (project.findProperty("threadCount") as? String)?.toIntOrNull() ?: 1

        parallel = parallelMode
        threadCount = threads

        val xmlName = if (project.hasProperty("suiteXml")) {
            project.property("suiteXml") as String
        } else {
            "All.xml"
        }

        val xmlPath = "src/test/resources/$xmlName"
        suites(xmlPath)
    }
}
