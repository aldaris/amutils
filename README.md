# AM debug utilities

The purpose of this application is to ease the debugging process of OpenAM by providing easily accessible debug utilities.
Currently implemented:

* Session decoding (retrieving server/site ID and storage key)
* Symmetric Encryption/decryption using the OpenAM encryption key

Future plans:
* Configuration analysis ("interesting" settings displayed from an export-svc-cfg)
* Configuration altering: modifying OpenAM server URL, passwords

## How to run it

In order to run this application just execute the following command:

    mvn jetty:run

The application should be up and running at http://localhost:7000

## License

Everything in this repo is licensed under the ForgeRock CDDL license: http://forgerock.org/license/CDDLv1.0.html
