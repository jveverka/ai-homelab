# Photos Database Backend

This is a simple database of photos. Photos are stored in folder which is accessible as file system.
This microservice will scan periodically given read-only folder with pictures, read EXIFF data for each picture such as:
* Shutter time, Aperture, ISO
* GPS location if available.
* Content of image (what is in the picture).
* File information like sha256 check sum, file type and file size and location.

Information about all pictures is stored in the mongo database to be accessed later by search engine.

