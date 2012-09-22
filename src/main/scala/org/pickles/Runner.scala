/**
 *
 */
package org.pickles

import org.pickles.crawler.DirectoryTreeCrawler

/**
 * @author jeffrey
 *
 * Discovers all files that need to be transformed and transofmrs them based on the user configuration
 */
class Runner {
  def run(configuration: Configuration) = {
    val directoryTreeCrawler = new DirectoryTreeCrawler
    val root = directoryTreeCrawler.crawl(configuration.featureFolder)

  }
}