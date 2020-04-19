import java.io.*;
import groovy.io.*;
import java.util.regex.*;
import Parser.AngularParser;
import Sorter.BuildSorter; 

def call(Map pipelineParams) {
    pipeline {
        agent none
 
        stages {
            stage('Build and Unit test') {
                steps {
                    script {
                        echo "clean verify"
                    }
                }
            }
        }
    }
}