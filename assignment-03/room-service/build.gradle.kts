/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.3.3/samples
 */

plugins {
    // Apply the java-library plugin to add support for Java Library
    java
    
    
    id ("java-library")
}

repositories {
    jcenter()
}


dependencies {

    implementation ("io.vertx:vertx-core:4.3.1")    
    implementation ("io.vertx:vertx-web:4.3.1")
    implementation ("io.vertx:vertx-web-client:4.3.1")
    implementation ("io.vertx:vertx-mqtt:4.3.1")

    // Use JUnit test framework
    testImplementation ("junit:junit:4.13.2")

	/* for logging */
	implementation ("org.slf4j:slf4j-api:1.7.25")
	implementation ("org.slf4j:slf4j-jdk14:1.7.36")
	
    implementation("io.github.java-native:jssc:2.9.4")
	
}