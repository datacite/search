# Upgrading underlying Solr

Read [Apache Solr Release Notes](https://github.com/apache/lucene-solr/blob/trunk/solr/CHANGES.txt) 
and apply all necessary changes. 

Change solr.version property in `pom.xml`.

## Checking overwritten classes

The following Solr classes are overwritten:

* org.apache.solr.handler.dataimport.FieldReaderDataSource 
* org.apache.solr.handler.dataimport.XPathEntityProcessor
* org.apache.solr.handler.dataimport.XPathRecordReader

You have to check if there are some upstream changes in theses classes.

They can be found upstream in [solr/contrib/dataimporthandler/src/java/org/apache/solr/handler/dataimport](https://github.com/apache/lucene-solr/tree/trunk/solr/contrib/dataimporthandler/src/java/org/apache/solr/handler/dataimport). 

