# File manager

* Functionality: this application allows you to process the file structure, import, save in a database and export
file system elements. 
The application has built with REST API. 

* Application logic: 
1. Using the createSomeNodes command (http://localhost:8080/smallApp/createSomeNodes), JSON with several files and folders is transferred to the application. The file system is being built (the relationship "parent -
child", parent folders have a list of child folders and files).
2. Using the createNode command (http://localhost:8080/smallApp/createNode), JSON with a file or folder is passed to the application.
3. Using the updateNode command (http://localhost:8080/smallApp/updateNode), JSON with the updated file or folder is sent to the application. If the parent folder changes, the file structure changes.
4. Using the getNode command (http://localhost:8080/smallApp/getNode/{id}), you can get the system element by id in the form of JSON. The element specifies the parent folder, child elements, if any, type, and size.
5. Using the getAllNodes command (http://localhost:8080/smallApp/getAllNodes) you can get all the elements of the file system as a JSON list.
6. Using the deleteNode command (http://localhost:8080/deleteNode/{id}), you can delete the selected system element. As a result, parent and child elements are changed in their respective parameters.

The specified structure is stored in the database (this application uses PostgreSQL).

* In case of changing or deleting elements of the file structure, the corresponding changes are automatically
are entered into the database.

* Work features: 

Initially, JSON is imported. It states:
- id of the file or folder
- type (FOLDER or FILE)
- size (if type - FOLDER - do not specify)
- parentID (parent folder id)
- URL

Re-imported elements update the current ones. Change item type from folder to file and from file to folder
not allowed. The order of the elements in the query is arbitrary.
	Main characteristics:
           - id of each element is unique among other elements
           - id field cannot be null
           - the parent of an element can only be a folder
           - belonging to the folder is determined by the parentId field
           - elements may not have a parent (when updating parentId to null, the element remains without a parent)
           - the url field should always be null when importing a folder
           - the size field should always be null when importing a folder
           - the size field for files must always be greater than or equal to 0
           - when updating an element, **all** their parameters are considered updated
           - There cannot be two elements with the same id in one request.