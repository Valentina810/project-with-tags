pipeline {
	agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде

    stages {
		// клонировать код из репозитория
        stage('Checkout') {
			steps {
				checkout scm
            }
        }

        // запустить сборку проекта через Gradle без запуска тестов
        stage('Build') {
			steps {
				sh './gradlew build -x test'
            }
        }
    }

    // этот этап выполняется всегда
    post {
	always {

	        //запуск тестов с выгрузкой результата в AllureTestOps
	        withAllureUpload(credentialsId: 'allure-credentials', name: '${JOB_NAME} - #${BUILD_NUMBER}', projectId: '34', results: [[path: 'build/allure-results']], serverId: 'AllureServer', tags: '') {
		      sh './gradlew test'
            }

			// сгенерировать локальный отчет Allure с очисткой предыдущего
            sh './gradlew allureReport --clean'

            // заархивировать результаты тестов, чтобы их можно было скачать
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true

            //опубликовать отчёт в Jenkins
            allure([results: [[path: 'build/allure-results']]])
        }
    }
}
