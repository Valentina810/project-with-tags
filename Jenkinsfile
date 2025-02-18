pipeline {
    agent any //any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде
    stages {
        // клонировать код из репозитория
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        // запустить сборку проекта через Gradle
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
        // запустить тесты
        stage('Run Tests') {
            steps {
                sh './gradlew test'
            }
        }
        // сгенерировать локальный отчет Allure
        stage('Generate Allure Report') {
            steps {
                sh './gradlew allureReport'
            }
        }
    }
}