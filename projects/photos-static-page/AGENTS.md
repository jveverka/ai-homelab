# Static Photo HTML Page
This project is about generating static html presentation from a folder with photographs in JPEG, JPG and PNG format.
* Input Folder - contains original photos. Original photos are read-only, must not be modified in any way. Static html page is generated in Output Folder.
* Input Folder structure - may contain folders representing photo collections. All subfolders must be scanned recursively. Unknown file formats will be skipped, corrupt files will be skipped. 
* Output Folder - contains resulting html presentation, html files, JavaScript and css files and photo thumbnails. Links to original photos are used to avoid full size picture duplication.

## Features of static html presentation
* Main page contains list of top-level subfolders in Input Folder.
* Each photo is scanned and following data is extracted:
  * EXIFF data if available: ISO, Shutter, Exposure time, Camera type, ...
  * Date and time when photo was created if available.
  * GPS coordinates where photo was taken if available.
  * Content of the photo - keywords what is in the picture.
* Data extracted from photos are saved in static json file.
* Simple search functionality is implemented in javascript so database can be searched.
* Following search filters are implemented
  * Search by time interval
  * Search by camera type
  * Search by location area
  * Search by photo content by keywords 

## Main page design
* Design of main page is simple.
* Search parameters are entered in top search bar.
