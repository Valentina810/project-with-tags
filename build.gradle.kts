plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("io.qameta.allure") version "2.12.0"
}

group = "com.github.valentina810"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")

    implementation("io.qameta.allure:allure-rest-assured:2.27.0")
    implementation("io.qameta.allure:allure-junit5:2.28.0")
}

tasks.test {
    useJUnitPlatform {
        val tags = System.getProperty("includeTags")
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() } ?: emptyList()

        if (tags.isNotEmpty() && "ALL" !in tags) {
            includeTags(*tags.toTypedArray())
        }
    }
}

// конфигурация плагина Allure
allure {
    report {
        // устанавливает версию Allure Report, используемую для генерации отчетов
        // эта версия влияет на визуализацию результатов тестирования
        version.set("2.32.2")
    }
}