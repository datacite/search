# Upgrading underlying Solr

The following Solr classes are overwritten:

* org.apache.solr.handler.dataimport.FieldReaderDataSource 
* org.apache.solr.handler.dataimport.XPathEntityProcessor
* org.apache.solr.handler.dataimport.XPathRecordReader

When upgrading to a new solr version (simply by changing the solr.version property in `pom.xml`), 
you have to check if there are some upstream changes in theses classes.

They can be found upstream in [solr/contrib/dataimporthandler/src/java/org/apache/solr/handler/dataimport](https://github.com/apache/lucene-solr/tree/trunk/solr/contrib/dataimporthandler/src/java/org/apache/solr/handler/dataimport). 

