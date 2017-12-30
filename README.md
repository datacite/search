# DataCite Search

[![Build Status](https://travis-ci.org/datacite/search.svg?branch=master)](https://travis-ci.org/datacite/search)

This is Search for metadata uploaded to DataCite's Metadata Store ([MDS](https://mds.datacite.org)). It is based on [Solr](http://lucene.apache.org/solr/).

To learn more about DataCite please visit [our website](http://www.datacite.org)

To use this software please go to [http://search.datacite.org](https://search.datacite.org)

## Installation

Using Docker.

```
docker run -p 8080:8080 datacite/search
```

You can now point your browser to `http://localhost:8080` and use the application.

For a more detailed configuration, including serving the application from the host for live editing, look at `docker-compose.yml` in the root folder.

Search a java servlet web application. You need a servlet container such as Tomcat, Maven and JDK (OpenJDK from Ubuntu works fine). All dependencies are managed by Maven public repositories.

### Solr Home Directory

Solr requires a home directory to store the index and some properties.
Be aware that the user running running your servlet container must have
write access to this directory.

The content of `solr_home` has to be copied to your selected solr home directory.

All other required files will be in the war-file.

### Configuration

The git repository has a number of `*.template` files, listed below. Those files are templates for the various configuration files, passwords, IP addresses etc.

If you are using Docker, the configuration files are generated automatically from variables in the `.env` file. Modify the variables in `.env.example` and rename it to `.env`.

For manual configuration you need to make a copy omitting .template from
file name, and adjust the values according to your local environment.

#### solr_home/collection1/conf/solrcore.properties

MDS database specific properties.

You will need to specify JDBC string, for example:

> jdbc:mysql://localhost:3306/datacite?useUnicode=true&characterEncoding=UTF8


#### src/main/webapp/META-INF/context.xml

Specify the path to your preliminarily chosen solr home directory by replacing `<dir>`.

#### src/main/resources/log4j.xml

Your usual log4j stuff.

### Securing Solr

Public access should only be granted for the `/public` path.

### Database Requirements

The following indexes are required on the database:

    create index dataset_version on metadata (dataset, metadata_version);
    create index dataset_updated on media (dataset, updated);

## Development

Follow along via [Github Issues](https://github.com/datacite/search/issues).

## Running

After deploying the following resources are of interest:

* `/public/ui` - search user interface
* `/` - admin interface

Data from MDS is imported via Solr's DataImportHandler. You can access it via the admin interface. Another option - especially useful for cron jobs - is `scripts/solr-client`. Simply try

    export SOLR_URL=http://localhost:8080/search
    scripts/solr-client import delta

for a delta import of MDS metadata. See `solr-client help` for usage.

### Memory and CPU monitoring

You can monitor memory and CPU using JConsole and a remote connection on the following host:

```
localhost:17264
```

### Note on Patches/Pull Requests

* Fork the project
* Write tests for your new feature or a test that reproduces a bug
* Implement your feature or make a bug fix
* Do not mess with Rakefile, version or history
* Commit, push and make a pull request. Bonus points for topical branches.

## License
**Search** is released under the [Apache 2 License](https://github.com/datacite/search/blob/master/LICENSE).
