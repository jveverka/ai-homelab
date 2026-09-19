# Requirements

## 1. Purpose
This is a simple Photo database. This microservice reads input directory with photos, scans new discovered photos and stores 

## 2. Functional requirements


### FR-001 Supported photo formats
Following photo formats are supported: JPEG, GIF, PNG
Photo is a file in the input folder.

### FR-002 Input folder scanning
Input read-only folder (file system) is scanned periodically, each photo is scanned for data and data is stored in the database. 
No changes to original photos are allowed. Only one scan process may run at the same time.

### FR-003 Data reading from photos
Following data is extracted from each photo:
* Shutter time, Aperture, ISO, .. 
* GPS location if available.
* Content of image (what is in the picture).
* File information like sha256 check sum, file type and file size and location.

