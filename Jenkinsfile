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

            // выгрузить отчет в Allure TestOps
            script {
				withCredentials([string(credentialsId: 'ALLURE_API_TOKEN', variable: 'ALLURE_TOKEN')]) {
					sh '''
                        # заархивировать результаты Allure
                        zip -r allure-results.zip build/allure-results

                        # выгрузить отчет в Allure TestOps
                        curl -X POST "${ALLURE_ENDPOINT}/api/testruns" \
                             -H "Authorization: Bearer ${ALLURE_TOKEN}" \
                             -F "projectId=${ALLURE_PROJECT_ID}" \
                             -F "file=@allure-results.zip"
                    '''
                }
            }

            // опубликовать отчёт в Jenkins
            allure([results: [[path: 'build/allure-results']]])
        }
    }
}