pipeline {
    agent any
    tools {
        gradle 'gradle 8.0'
    }
    stages {
        stage('Build Gradle') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mittulmandhan/bidding-server']])
                dir('service-registry') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build --stacktrace'
                }
                dir('cloud-config-server') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build --stacktrace'
                }
                dir('cloud-gateway') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build --stacktrace'
                }
                dir('security-service') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build --stacktrace'
                }
                dir('auction-service') {
                    sh './gradlew clean build --stacktrace'
                }
            }
        }
        stage('build docker image') {
            steps {
                dir('service-registry') {
                        script {
                            sh 'docker build -t mittulmandhan/service-registry .'
                        }
                }
                dir('cloud-config-server') {
                        script {
                            sh 'docker build -t mittulmandhan/cloud-config-server .'
                        }
                }
                dir('cloud-gateway') {
                        script {
                            sh 'docker build -t mittulmandhan/cloud-gateway .'
                        }
                }
                dir('security-service') {
                        script {
                            sh 'docker build -t mittulmandhan/security-service .'
                        }
                }
                dir('auction-service') {
                        script {
                            sh 'docker build -t mittulmandhan/auction-service .'
                        }
                }
            }
        }
        stage('push image to docker hub') {
            steps {
                dir('service-registry') {
                        script {
                            withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                                sh 'docker login -u mittulmandhan -p ${dockerhubpwd}'
                                sh 'docker push mittulmandhan/service-registry'
                            }
                        }
                }
                dir('cloud-config-server') {
                        script {
                            withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                                sh 'docker login -u mittulmandhan -p ${dockerhubpwd}'
                                sh 'docker push mittulmandhan/cloud-config-server'
                            }
                        }
                }
                dir('cloud-gateway') {
                        script {
                            withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                                sh 'docker login -u mittulmandhan -p ${dockerhubpwd}'
                                sh 'docker push mittulmandhan/cloud-gateway'
                            }
                        }
                }
                dir('security-service') {
                        script {
                            withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                                sh 'docker login -u mittulmandhan -p ${dockerhubpwd}'
                                sh 'docker push mittulmandhan/security-service'
                            }
                        }
                }
                dir('auction-service') {
                        script {
                            withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                                sh 'docker login -u mittulmandhan -p ${dockerhubpwd}'
                                sh 'docker push mittulmandhan/auction-service'
                            }
                        }
                }
            }
        }
    }
}