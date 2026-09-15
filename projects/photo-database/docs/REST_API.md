# REST API
Base path: /api/v1
Content type: application/json (requests and success responses)
Error responses: application/problem+json (RFC 9457 Problem Details)

## Input Folder scanner APIs

### Start scan
POST /scanner
Request:
```json
{
   "subDirectory": "path/to/subdirectory",
   "skipScannedFiles": true
}
```

### Get scan status
GET /scanner
Response:
```json
{
  "status": "running",
  "scanned": 156454
}
```

### Stop scan
DELETE /scanner

## Files API

### Get database statistics
GET /files/statistics
Response:
```json
{
   "files": 12354444,
   "totalSizeBytes": 654698795465
}
```