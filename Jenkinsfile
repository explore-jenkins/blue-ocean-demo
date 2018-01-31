pipeline {
  agent {
    node {
      label 'worker_node1'
    }
    
  }
  stages {
    stage('Source') {
      steps {
        git 'https://github.com/explore-jenkins/blue-ocean-demo.git'
      }
    }
    stage('Build') {
      steps {
        tool 'gradle4'
        sh 'gradle build'
      }
    }
  }
  environment {
    COMPLETED_MSG = 'Build done!'
  }
}