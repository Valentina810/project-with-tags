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
				steps {
					script {
						withCredentials([string(credentialsId: 'ALLURE_API_TOKEN', variable: 'ALLURE_TOKEN')]) {
							sh '''
                                # удалить старый allurectl, если он есть
                                if [ -f /usr/local/bin/allurectl ]; then
                                    rm -f /usr/local/bin/allurectl
                                fi

                                # скачать новый allurectl
                                curl -o /usr/local/bin/allurectl -L https://github.com/allure-framework/allurectl/releases/latest/download/allurectl-linux
                                chmod +x /usr/local/bin/allurectl

                                # allurectl работает?
                                /usr/local/bin/allurectl --version || (echo "Ошибка: Allurectl не работает!" && exit 1)

                                # загрузить отчёт
                                export PATH=$PATH:/usr/local/bin
                                /usr/local/bin/allurectl upload \
                                    --endpoint ${ALLURE_ENDPOINT} \
                                    --project-id ${ALLURE_PROJECT_ID} \
                                    --token ${ALLURE_TOKEN} \
                                    --results build/allure-results
                            '''
                        }
                    }
                }

            // заархивировать результаты тестов, чтобы их можно было скачать
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true

            // опубликовать отчёт в Jenkins
            allure([results: [[path: 'build/allure-results']]])
        }
    }
}
