# edge-ncip
Edge API for mod-ncip


**README DRAFT** in progress...adding code and documentation to GIT


## Overview
The purpose of this module is to expose mod-ncip to external applications/3rd party software.  It simply passes requests through to the NCIP module (after the API key is authenticated).  It does not contain any other functionality

## Security & Configuration
The edge-ncip module is secured via the functionality provided by the edge-common project (via API key).  

The configuration for this module conforms to the edge-common project.


https://github.com/folio-org/edge-common

## endpoint

.../circapi/*