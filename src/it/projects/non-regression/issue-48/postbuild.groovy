/**
 * checksum-maven-plugin
 * Copyright © 2010-2021 checksum-maven-plugin contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.openmrs.maven.plugins.checksum.test.integration.PostBuildScriptHelper
import org.apache.maven.artifact.versioning.ComparableVersion

try
{
  // Instantiate a helper.
  PostBuildScriptHelper helper = new PostBuildScriptHelper( basedir, localRepositoryPath, context )

  // Fail if there are warnings
  helper.assertBuildLogDoesNotContain('[WARNING]')
  helper.assertBuildLogDoesNotContain('[ERROR]')

  // Fail if no traces of checksum-maven-plugin invocation.
  helper.assertBuildLogContains( "checksum-maven-plugin" );

  // Check files have been created and are not empty.
  helper.assertFileIsNotEmpty( "target/issue-48-1.0.0-SNAPSHOT.jar.md5" )
  helper.assertFileIsNotEmpty( "target/issue-48-1.0.0-SNAPSHOT.jar.sha1" )
  helper.assertFileIsNotEmpty( "target/issue-48-1.0.0-SNAPSHOT.jar.sha256" )

  // Check files have been installed.
  helper.assertFileIsNotEmptyInLocalRepo( "org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT/issue-48-1.0.0-SNAPSHOT.jar.md5" )
  helper.assertFileIsNotEmptyInLocalRepo( "org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT/issue-48-1.0.0-SNAPSHOT.jar.sha1" )
  helper.assertFileIsNotEmptyInLocalRepo( "org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT/issue-48-1.0.0-SNAPSHOT.jar.sha256" )

  // Check files have been deployed.
  helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.md5" )
  helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.sha1" )
  helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.sha256" )

  // Maven < 3.9.0 also deploys a checksum for every deployed file, including the attached
  // checksum files themselves (e.g. issue-48-1.0.0-*.md5.md5). Maven 3.9.0+ no longer
  // generates checksums for checksum files, so only assert these on older Maven.
  if ( new ComparableVersion( mavenVersion ) < new ComparableVersion( "3.9.0" ) )
  {
    helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.md5.md5" )
    helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.sha1.md5" )
    helper.assertFileExists( "target/deploy-repository/org/openmrs/maven/plugins/checksum/test/projects/issue-48/1.0.0-SNAPSHOT", "issue-48-1.0.0-*.sha256.md5" )
  }

}
catch ( Exception e )
{
  System.err.println( e.getMessage() )
  return false;
}
