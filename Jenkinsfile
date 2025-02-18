pipeline {
    agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде

    stages {
        // клонировать код из репозитория
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // очистить проект перед сборкой и запустить сборку через Gradle
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        // запустить тесты
        stage('Run Tests') {
            steps {
                sh './gradlew test'
            }
        }
    }

    // этот этап выполняется всегда, даже если тесты упали
    post {
        always {
            // сгенерировать локальный отчет Allure с очисткой предыдущего
            sh './gradlew allureReport --clean'

            // заархивировать результаты тестов, чтобы их можно было скачать
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true
        }
    }
}
