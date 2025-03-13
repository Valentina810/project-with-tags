properties([
    parameters([
         string(
                name: 'BRANCH',
                defaultValue: 'master',
                description: 'Введите название ветки, из которой нужно собрать проект'
            ),

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
                        def tags = ["ALL", "MANY_ENTITIES", "ENTITY", "TIME", "SMOKE", "ENTITIES"]
                        return tags.isEmpty() ? "" : tags
                        """
             ]
         ],
         defaultValue: ''
        ]
    ])
])

pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                 script {
                    echo "Сборка из ветки: ${params.BRANCH}"
                 }
                 checkout([
                    $class: 'GitSCM',
                     branches: [[name: "*/${params.BRANCH}"]],
                      userRemoteConfigs: [[
                        url: 'https://github.com/Valentina810/project-with-tags.git'
                      ]]
                 ])
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
                    sh "./gradlew test ${params.includeTags.contains('ALL') ? '' : "-DincludeTags=${params.includeTags.replaceAll('\\s+', '')}"}"
                }
            }
        }
    }

    post {
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
