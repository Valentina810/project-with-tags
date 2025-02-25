pipeline {
	agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде

    stages {
		stage('Checkout') {
			steps {
				// клонировать код из репозитория
				checkout scm
            }
        }
    }
    stages {
		stage('Run tests') {
			steps {
				//запуск тестов с выгрузкой результата в AllureTestOps
                withAllureUpload(credentialsId: 'allure-credentials', name: '${JOB_NAME} - #${BUILD_NUMBER}', projectId: '34', results: [[path: 'build/allure-results']], serverId: 'AllureServer', tags: '') {
					sh './gradlew test'
                }
            }
        }
    }
    post {// этот этап выполняется всегда
        always {
		// сгенерировать локальный отчет Allure с очисткой предыдущего
            sh './gradlew allureReport --clean'

            // заархивировать результаты тестов, чтобы их можно было скачать
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true

            //опубликовать отчёт в Jenkins
            allure([results: [[path: 'build/allure-results']]])
        }
    }
}