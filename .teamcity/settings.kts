import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.golang
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.10"

project {

    vcsRoot(HttpsGithubComLqzengHelloGoTeamcityGitRefsHeadsMain)

    buildType(Build)

    params {
        password("TestParameter", "cks408970c2652e0a3b1cdeb55649144c80Q7kT4huNLq/UYpxWGajzbg==", display = ParameterDisplay.HIDDEN)
    }

    features {
        dockerRegistry {
            id = "PROJECT_EXT_2"
            name = "Docker Registry"
            userName = "elmanzeeguy"
            password = "cks408970c2652e0a3b1cdeb55649144c8095Y/fo9OiRcrW8mCHyJ3Mg=="
        }
    }
}

object Build : BuildType({
    name = "Build"

    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        password("TestParameter", "cks408970c2652e0a3b1cdeb55649144c80VxcyAHfS9tWNPvzH9e/RWw==", display = ParameterDisplay.HIDDEN)
    }

    vcs {
        root(HttpsGithubComLqzengHelloGoTeamcityGitRefsHeadsMain)
    }

    steps {
        dockerCommand {
            name = "build image"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = "elmanzeeguy/teamcity-hello-go:1.%build.number%"
                commandArgs = "--pull"
            }
        }
        dockerCommand {
            name = "push image"
            commandType = push {
                namesAndTags = "elmanzeeguy/teamcity-hello-go:1.%build.number%"
                removeImageAfterPush = false
            }
        }
        script {
            name = "command line"
            scriptContent = """
                #!/bin/bash
                
                echo "script started"
                docker images
                docker run -dp 8080:8080 elmanzeeguy/teamcity-hello-go:1.%build.number%
                curl http://localhost:8080
                echo "script finished"
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        golang {
        }
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_2"
            }
        }
    }
})

object HttpsGithubComLqzengHelloGoTeamcityGitRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/lqzeng/hello-go-teamcity.git#refs/heads/main"
    url = "https://github.com/lqzeng/hello-go-teamcity.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})
