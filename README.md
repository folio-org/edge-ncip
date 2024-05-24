# edge-ncip

Copyright (C) 2019-2023 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

Edge API for mod-ncip

## Overview

The purpose of this module is to expose mod-ncip to external applications/3rd party software. It simply passes requests
through to the NCIP module (after the API key is authenticated). It does not contain any other functionality

## Permissions

Institutional users should be granted the following permissions in order to use this edge API (because these are
required for the NCIP module itself):

```
    ncip.all
    inventory-storage.items.collection.get
    ui-circulation.settings.overdue-fines-policies
    ui-circulation.settings.lost-item-fees-policies
    automated-patron-blocks.collection.get
    circulation-storage.circulation-rules.get
    manualblocks.collection.get
    
```

### IMPORTANT NOTE ABOUT INSTITUTIONAL USER - It has to be assigned a patron group. There is an issue with create item which requires the user to be assigned a patron group.

## Security Configuration

Configuration information is specified in two forms:

1. System Properties - General configuration
2. Properties File - Configuration specific to the desired secure store

### System Properties

| Property                  | Default             | Description                                                         |
|---------------------------|---------------------|---------------------------------------------------------------------|
| `port`                    | `8081`              | Server port to listen on                                            |
| `okapi_url`               | *required*          | Where to find Okapi (URL)                                           |
| `secure_store`            | `Ephemeral`         | Type of secure store to use.  Valid: `Ephemeral`, `AwsSsm`, `Vault` |
| `secure_store_props`      | `NA`                | Path to a properties file specifying secure store configuration     |
| `token_cache_ttl_ms`      | `3600000`           | How long to cache JWTs, in milliseconds (ms)                        |
| `null_token_cache_ttl_ms` | `30000`             | How long to cache login failure (null JWTs), in milliseconds (ms)   |
| `token_cache_capacity`    | `100`               | Max token cache size                                                |
| `log_level`               | `INFO`              | Log4j Log Level                                                     |
| `request_timeout_ms`      | `30000`             | Request Timeout                                                     |
| `api_key_sources`         | `PARAM,HEADER,PATH` | Defines the sources (order of precedence) of the API key.           |

### Env variables for TLS configuration for Http server

To configure Transport Layer Security (TLS) for the HTTP server in an edge module, the following configuration
parameters should be used.
Parameters marked as Required are required only in case when ssl_enabled is set to true.

| Property                                | Default           | Description                                                                      |
|-----------------------------------------|-------------------|----------------------------------------------------------------------------------|
| `FOLIO_CLIENT_TLS_ENABLED`              | `false`           | Set whether SSL/TLS is enabled for Vertx Http Server                             |
| `FOLIO_CLIENT_TLS_TRUSTSTORETYPE`       | `NA`              | Set the type of the keystore. Common types include `JKS`, `PKCS12`, and `BCFKS`  |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPATH`       | `NA`              | Set the location of the keystore file in the local file system                   |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPASSWORD`   | `NA`              | Set the password for the keystore                                                |

### Env variables for TLS configuration for Web Client

To configure Transport Layer Security (TLS) for Web clients in the edge module, you can use the following configuration
parameters.
Truststore parameters for configuring Web clients are optional even when ssl_enabled = true.
If truststore parameters need to be populated, truststore_type, truststore_path and truststore_password are required.

| Property                                | Default           | Description                                                                      |
|-----------------------------------------|-------------------|----------------------------------------------------------------------------------|
| `FOLIO_CLIENT_TLS_ENABLED`              | `false`           | Set whether SSL/TLS is enabled for Vertx Http Server                             |
| `FOLIO_CLIENT_TLS_TRUSTSTORETYPE`       | `NA`              | Set the type of the keystore. Common types include `JKS`, `PKCS12`, and `BCFKS`  |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPATH`       | `NA`              | Set the location of the keystore file in the local file system                   |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPASSWORD`   | `NA`              | Set the password for the keystore                                                |

## Additional information

There will be a single instance of okapi client per OkapiClientFactory and per tenant,
which means that this client should never be closed or else there will be runtime errors.
To enforce this behaviour, method close() has been removed from OkapiClient class.

## Endpoints (you can use either)

.../ncip/yourapikeygoeshere <br>
.../ncip?apikey=yourapikeygoeshere

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