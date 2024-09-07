File Management API.
This project provides a RESTful API for file management operations. It includes functionalities for uploading, updating, retrieving, listing, and deleting files.

Table of Contents
1. Prerequisites
2. Neccessary Dependency
3. Connecting with MYSQL Server using Docker Desktop
4. Endpoints
5. Testing with Postman

1. Prerequisite
Java 11 or later
Maven 3.6.3 or later
Docker Desktop to connect with Database


3. Neccessary Dependency


   
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>

4. Connecting with MYSQL Server using Docker Desktop

   spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dropboxdb
    username: root
    password: root
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
  application:
    name: dropbox

using Docker Command to connect with MYSQL Docker Server with an application:
docker run -p 3306:3306 --name dropboxdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=dropboxdb -d mysql

5. Endpoints
Upload File
Endpoint: POST /files/upload
Description: Upload a new file.
Input: Multipart file (file parameter).
Output: JSON response with file ID and status.

Update Metadata
Endpoint: PUT /files/{fileId}
Description: Update file metadata.
Input: JSON body with metadata fields (e.g., fileName, fileType, size, createdAt).
Output: JSON response with status and updated file ID.

Retrieve File
Endpoint: GET /files/{fileId}
Description: Retrieve a file by its ID.
Output: Binary file data with appropriate headers (Content-Disposition and Content-Type).

List Files
Endpoint: GET /files
Description: List all files with their metadata.
Output: JSON array of file metadata.

Delete File
Endpoint: DELETE /files/{fileId}
Description: Delete a file by its ID.
Output: Success or failure message.



5. Testing with Postman

1. Upload File
Set Method: POST
URL: http://localhost:8080/files/upload
Headers: Content-Type: multipart/form-data
Body: Choose form-data, add a key file of type File, and select the file to upload.
Send: Click Send and check the response.


2. Update Metadata
Set Method: PUT
URL: http://localhost:8080/files/{fileId} (replace {fileId} with the actual file ID)
Headers: Content-Type: application/json
Body: Choose raw and select JSON format. Enter JSON payload for metadata

{
  "fileName": "updated-file-name.txt",
  "fileType": "text/plain",
  "size": 54321
}
Send: Click Send and check the response.


3. Retrieve File
Set Method: GET
URL: http://localhost:8080/files/{fileId} (replace {fileId} with the actual file ID)
Headers: None required
Send: Click Send to download the file.


4. List Files
Set Method: GET
URL: http://localhost:8080/files
Headers: None required
Send: Click Send and check the response for the list of files.


5. Delete File
Set Method: DELETE
URL: http://localhost:8080/files/{fileId} (replace {fileId} with the actual file ID)
Headers: None required
Send: Click Send and check the response for success or failure message.
