pipeline {
	agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде
	parameters {
       multiChoice(name: 'includeTags', choices: ['MANY_ENTITIES', 'TIME', 'SMOKE', 'ENTITIES'], description: 'Выберите теги тестов')
    }

    stages {
		stage('Checkout') {
			steps {
				// клонировать код из репозитория
				checkout scm
            }
        }

		stage('Run tests') {
			steps {
			    script {
                    echo "Переданные теги: '${params.includeTags}'"
                }
				//запуск тестов с выгрузкой результата в AllureTestOps
				//дополнили запуском тестов только с указанными тэгами
                withAllureUpload(credentialsId: 'allure-credentials', name: '${JOB_NAME} - #${BUILD_NUMBER}', projectId: '34', results: [[path: 'build/allure-results']], serverId: 'AllureServer', tags: '') {
                    sh "./gradlew test -DincludeTags=${params.includeTags.replaceAll('\\s+', '')}"
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