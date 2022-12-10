
import com.typesafe.sbt.packager.MappingsHelper._
import com.typesafe.sbt.packager.docker.{Cmd, DockerStageBreak, ExecCmd}


lazy val server  = (project in file("server"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "docker-react-poc",
    version := "0.1",
    scalaVersion := "2.13.8",
    fork := true,
    //We include the source files for the webapps in the docker mappings but not the build files
    (Docker / mappings) ++=
      directory(baseDirectory.value / ".." / "webapps").flatMap{
        case (f,s) if !s.contains("/node_modules") && !s.contains("/build")=> Some((f,s))
        case _ => None
      },
    //We exclude the files in the resources that we use to develop
    (Compile / packageBin / mappings) :=
      (Compile / packageBin / mappings)
        .value.filter(!_._1.getPath.contains("resources/public/webapps")),
    dockerExposedPorts ++= Seq(9000, 9001),
    dockerCommands := {
      val webappsSeq = Seq("react-app-1", "react-app-2")
      val nodeImage = "node:18-alpine"
      val jdkImage = "eclipse-temurin:11.0.15_10-jdk-focal"
      val jreImage = "eclipse-temurin:11.0.15_10-jre-focal"
      val jarName = {
        val moduleId = projectID.value
        val artifactV = artifact.value
        moduleId.organization + "." + artifactV.name + "-" + moduleId.revision +
          artifactV.classifier.fold("")("-" + _) + "." + artifactV.extension
      }

      //This stage builds the webapps
      Seq(
        Cmd("FROM", s"$nodeImage AS webappBuilder")
        , Cmd("LABEL", """snp-multi-stage="intermediate"""")
        , Cmd("WORKDIR", "/usr/src/app")
        , Cmd("COPY", "webapps", "webapps")
      ) ++
      webappsSeq.flatMap{
        webappName =>
          Seq(
            Cmd("WORKDIR", s"/usr/src/app/webapps/$webappName")
            , Cmd("RUN", "npm ci --silent")
            , Cmd("RUN", "npm run --silent build")
            , Cmd("RUN", "mkdir", "-p", s"/usr/src/app/webapps/build/$webappName", "&&", "cp", "-r", s"/usr/src/app/webapps/$webappName/build/*", s"/usr/src/app/webapps/build/$webappName")
          )
      } ++
      Seq(DockerStageBreak) ++
      //This stage copies the webapp build files into the container and then updates the jar with them
      Seq(
        Cmd("FROM", s"$jdkImage AS stage0")
        , Cmd("LABEL", """snp-multi-stage="intermediate"""")
        , Cmd("COPY", "1/opt", "/1/opt")
        , Cmd("COPY", "2/opt", "/2/opt")
        , Cmd("USER", "root")
        , Cmd("COPY", "--from=webappBuilder", "/usr/src/app/webapps/build", "/opt/ext_resources/public/webapps")
        , ExecCmd("RUN", "jar", "uf", s"/2/opt/docker/lib/$jarName", "-C", "/opt/ext_resources/", ".")
        , ExecCmd("RUN", "chmod", "-R", "u=rX,g=rX", "/1/opt/docker")
        , ExecCmd("RUN", "chmod", "-R", "u=rX,g=rX", "/2/opt/docker")
      ) ++
      Seq(DockerStageBreak) ++
      //We copy the result into the final container
      Seq(
        Cmd("FROM", s"$jreImage AS mainstage")
        , Cmd("USER", "root")
        , Cmd("RUN", "id", "-u", "demiourgos728 1>/dev/null 2>&1 || (( getent group 0 1>/dev/null 2>&1 || ( type groupadd 1>/dev/null 2>&1 && groupadd -g 0 root || addgroup -g 0 -S root )) && ( type useradd 1>/dev/null 2>&1 && useradd --system --create-home --uid 1001 --gid 0 demiourgos728 || adduser -S -u 1001 -G root demiourgos728 ))")
        , Cmd("WORKDIR", "/opt/docker")
        , Cmd("COPY", "--from=stage0", "--chown=demiourgos728:root", "/1/opt/docker", "/opt/docker")
        , Cmd("COPY", "--from=stage0", "--chown=demiourgos728:root", "/2/opt/docker", "/opt/docker")
        , Cmd("EXPOSE", dockerExposedPorts.value.mkString(" "))
        , Cmd("USER", "1001:0")
        , ExecCmd("ENTRYPOINT", "/opt/docker/bin/docker-react-poc")
        , ExecCmd("CMD")
      )
    }
  )
  .settings(
    libraryDependencies ++=
      Dependencies.akkaHttp
  )