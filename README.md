# profix

This Clojure CLI calls Command Central Rest API and queries data about available and installed products and fixes.

The goal of this project is to integrate it with an automation tool such as Ansible to facilitate fixes and product managements.

## Usage

Download the profix jar, and then use it as follow :
    
    java -jar profix-1.0.0-rc0-standalone.jar -f MY_FIXES_REPO -s MY_ADMIN_PWD list-available-fixes myNCCEodeAlias
    
Options are as follow :

```
Usage: profix [options] action [arguments]

Options:
  -H, --hostname HOST      localhost  Command Central host
  -p, --port PORT          8090       Port number
  -t, --protocol PROTOCOL  http       Protocol
  -f, --fixesRepo FIXES    fixes      Fixes repository
  -m, --platform PLATFORM  LNXAMD64   Platform
  -s, --secret PASSWORD    manage     Administrator secret
  -h, --help

Actions:
  list-available-fixes <nodeAlias>    List the available fixes for a given node alias
```

## License

Copyright © 2017 Alexandre Fruchaud

Distributed under the Eclipse Public License either version 1.0 or any later version.
