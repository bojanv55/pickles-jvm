/**
 *
 */
package org.pickles.features

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.pickles.crawler.FileSystemBuilder
import java.io.PrintWriter
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author jeffrey
 *
 */
@RunWith(classOf[JUnitRunner])
class PicklesParserSpec extends FunSpec with ShouldMatchers {
  describe("A PicklesParser") {
    it("should be able to parse a properly formatted feature file") {
      val feature =
        """
        @accepted
        Feature: A test feature
          In order to ensure all features are properly parsed
          As a developer
          I want to  test the PicklesParser class to ensure it can properly read and parse content from a file
        
          @slow
          Background: Some facts about parsing
            Here is some context about the background
            
            Given a precondition
            And another precondition
        
          @ignored
          Scenario: A scenario
            Here is some context about the scenario
            
            Given some other precondition with a table
            | column 1 | column 2 | 
            | value 1  | value 2  |
            When this thing happens
            Then this postcondition is present
        
          @outline
          Scenario Outline: A scenario outline
            Here is some context about the scenario outline
        
            Given yet another precondition
            When this <thing> happens
            Then this postcondition is present
        
            Examples: where thing is good
            | thing  |
            | good   |
            | gooder |
        
            Examples: where thing is bad
            | thing  |
            | bad    |
            | badder |
        """

      val builder = FileSystemBuilder.build()
      val featureFile = builder.addFile("ram://feature.txt")
      val featureFileOutputStream = featureFile.getContent().getOutputStream()
      val writer = new PrintWriter(featureFileOutputStream)
      writer.write(feature)
      writer.flush()
      writer.close()
      featureFileOutputStream.close()

      val parser = new PicklesParser()
      val parsedFeature = parser.parse(featureFile)

      parsedFeature should not be (null)
      parsedFeature.name should equal("A test feature")
      parsedFeature.description should equal(
        """In order to ensure all features are properly parsed
As a developer
I want to  test the PicklesParser class to ensure it can properly read and parse content from a file""")

      /*parsedFeature.getTags should have size (1)
      parsedFeature.getTags.head should equal("@accepted")*/

      parsedFeature.background should be('defined)
      val background = parsedFeature.background.get
      background should have(
        'name("Some facts about parsing"),
        'description("Here is some context about the background"))

      parsedFeature.scenarios should have size (1)
      parsedFeature.scenarios.head should have(
        'name("A scenario"),
        'description("Here is some context about the scenario"))

      parsedFeature.scenarioOutlines should have size (1)
      parsedFeature.scenarioOutlines.head should have(
        'name("A scenario outline"),
        'description("Here is some context about the scenario outline"))

    }
  }
}