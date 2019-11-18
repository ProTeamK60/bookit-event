cp .mvn/wrapper/settings.xml ~/.m2/settings.xml
echo "<settingsSecurity><master>${maven_security_master}</master></settingsSecurity>" > ~/.m2/settings-security.xml
./mvnw compile jib:build -B -DskipTests=true \
              -Dbuild.number=${CIRCLE_BUILD_NUM} \
              -Dcommit.hash=${CIRCLE_SHA1} \
              -Dcircle.workflow.guid=${CIRCLE_WORKFLOW_ID} \
              -Dbuild.user=${CIRCLE_PROJECT_USERNAME} \
              -Dbuild.repo=${CIRCLE_PROJECT_REPONAME}
