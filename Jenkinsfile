pipeline {
    agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде

    parameters {
       string(name: 'ALLURE_ENDPOINT', description: 'Адрес Allure TestOps')
       string(name: 'ALLURE_PROJECT_ID', description: 'ID проекта в Allure TestOps')
       credentials(name: 'ALLURE_TOKEN', description: 'API-токен из Jenkins Credentials')
    }

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

            // выгрузить отчет в AllureTestOps
            sh """
                allurectl upload \
                --endpoint ${params.ALLURE_ENDPOINT} \
                --project-id ${params.ALLURE_PROJECT_ID} \
                --token ${params.ALLURE_TOKEN} \
                --results build/allure-results
            """

            // заархивировать результаты тестов, чтобы их можно было скачать
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true

            //опубликовать отчёт в Jenkins
            allure([results: [[path: 'build/allure-results']]])
        }
    }
}
