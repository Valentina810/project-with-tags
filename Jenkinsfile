properties([
    parameters([
        [$class: 'CascadeChoiceParameter',
         choiceType: 'PT_CHECKBOX',  // тип выбора: многократный выбор с флажками
         description: 'Выберите теги JUnit 5 для тестов',
         name: 'includeTags',  // имя параметра
         script: [
             $class: 'GroovyScript',
             script: [
                 classpath: [],
                 sandbox: false,
                 script: """
                    return ["MANY_ENTITIES", "ENTITY", "TIME", "SMOKE", "ENTITIES"]
                 """
             ]
         ]
        ],
        defaultValue: ''
    ])
])

pipeline {
	agent any // any означает, что Jenkins может запустить этот пайплайн на любой доступной ноде

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
                withAllureUpload(
                    credentialsId: 'allure-credentials',
                    name: '${JOB_NAME} - #${BUILD_NUMBER}',
                    projectId: '34',
                    results: [[path: 'build/allure-results']],
                    serverId: 'AllureServer',
                    tags: params.includeTags ? params.includeTags.split(',').collect { it.trim() }.join(',') : ''
                ) {
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