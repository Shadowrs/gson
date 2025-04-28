import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.vanniktech.publish)
}

group = "com.google.code.gson"
version = "2.11.0-dmm-1.0.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.errorprone:error_prone_annotations:2.35.1")
}

sourceSets {
    getByName("main") {
        java.srcDir("src/main/java-templates")
    }
}

tasks.test {
    useJUnitPlatform()
}
publishing {
    repositories {
        // Support for GitHub Packages publishing.
        // We do not directly use this, it is only included to make publishing for forks easier.
        maven {
            name = "GitHubPackages"
            // Change the organization and project URL to match with where you're publishing.
            url = uri("https://maven.pkg.github.com/dmmdevv/dmm-maven")
            credentials {
                // The gpr.user and gpr.key properties should be defined where your `gradle.properties`
                // file is stored, which is typically at `user.home/.gradle/gradle.properties`
                // IntelliJ does support overriding the Gradle user home in
                // Build, Execution, Deployment -> Build Tools -> Gradle
                // So the gradle.properties path may differ. If the file doesn't exist, create it
                // and fill in the user (GitHub username) and key (GitHub Personal Access Token) as shown below.
                // Personal access tokens can be generated at https://github.com/settings/tokens
                // Only the packages:read and packages:write permissions are required.
                username = project.findProperty("dmm.gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("dmm.gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
mavenPublishing {
    pom {
        url = "https://github.com/dmmdevv/dmm-maven"
        inceptionYear = "2025"
    }
}
tasks.withType<ShadowJar>().configureEach {
    archiveClassifier = ""
}