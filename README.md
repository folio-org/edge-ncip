# edge-ncip

Copyright (C) 2019-2023 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

Edge API for mod-ncip

## Overview
The purpose of this module is to expose mod-ncip to external applications/3rd party software.  It simply passes requests through to the NCIP module (after the API key is authenticated).  It does not contain any other functionality

## Permissions
Institutional users should be granted the following permissions in order to use this edge API (because these are required for the NCIP module itself):
```
    ncip.all
    inventory-storage.items.collection.get
    ui-circulation.settings.overdue-fines-policies
    ui-circulation.settings.lost-item-fees-policies
    automated-patron-blocks.collection.get
    circulation-storage.circulation-rules.get
    manualblocks.collection.get
    
```
### IMPORTANT NOTE ABOUT INSTITUTIONAL USER - It has to be assigned a patron group.  There is an issue with create item which requires the user to be assigned a patron group.

## Security & Configuration
The edge-ncip module is secured via the functionality provided by the edge-common project (via API key).  

The configuration for this module conforms to the edge-common project.

https://github.com/folio-org/edge-common

### System Properties

| Property               | Default           | Description                                                             |
|------------------------|-------------------|-------------------------------------------------------------------------|
| `port`                 | `8081`            | Server port to listen on                                                |
| `okapi_url`            | *required*        | Where to find Okapi (URL)                                               |
| `request_timeout_ms`   | `30000`           | Request Timeout                                                         |
| `ssl_enabled`          | `false`           | Set whether SSL/TLS is enabled for Vertx Http Server                    |
| `keystore_type`        | `NA`              | Set the key store type                                                  |
| `keystore_provider`    | `NA`              | Set the provider name of the key store                                  |
| `keystore_path`        | `NA`              | Set the path to the key store                                           |
| `keystore_password`    | `NA`              | Set the password for the key store                                      |
| `key_alias`            | `NA`              | Optional identifier that points to a specific key within the key store  |
| `key_alias_password`   | `NA`              | Optional param that points to a password of `key_alias` if it protected |
| `log_level`            | `INFO`            | Log4j Log Level                                                         |
| `token_cache_capacity` | `100`             | Max token cache size                                                    |
| `token_cache_ttl_ms`   | `100`             | How long to cache JWTs, in milliseconds (ms)                            |
| `secure_store`         | `Ephemeral`       | Type of secure store to use.  Valid: `Ephemeral`, `AwsSsm`, `Vault`     |
| `secure_store_props`   | `NA`              | Path to a properties file specifying secure store configuration         |


## endpoints (you can use either)

.../ncip/yourapikeygoeshere <br>
.../ncip?apikey=yourapikeygoeshere

## Additional information

### Issue tracker

See project [EDGNCIP](https://issues.folio.org/browse/EDGNCIP)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

### ModuleDescriptor

See the built `target/ModuleDescriptor.json` for the interfaces that this module
requires and provides, the permissions, and the additional module metadata.

### Code analysis

[SonarQube analysis](https://sonarcloud.io/dashboard?id=org.folio%3Aedge-ncip).

### Download and configuration

The built artifacts for this module are available.
See [configuration](https://dev.folio.org/download/artifacts) for repository access,
and the [Docker image](https://hub.docker.com/r/folioorg/edge-ncip/).

### Other documentation

Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at [dev.folio.org](https://dev.folio.org/)

