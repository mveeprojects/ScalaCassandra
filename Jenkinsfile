pipeline {
    agent any

    stages {

        stage('Compile') {
            steps {
                sh '''#!/bin/bash
                	echo "Compiling code..."
                	./sbt compile
                	echo "Done compiling"
         		'''
            }
        }

        stage('Test') {
            steps {
                sh '''#!/bin/bash
                	echo "Testing code..."
                	./sbt test
                	echo "Done testing"
         		'''
            }
        }

        stage('Create Docker Image and Run') {
            steps {
                sh '''#!/bin/bash
                    echo "Redeploying the app ..."
                    ./sbt redeployApp
                    echo "App has been deployed"
                '''
            }
        }
    }
}