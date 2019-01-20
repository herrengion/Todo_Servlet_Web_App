****************************************************************************************************
----------------------------------------------------------------------------------------------------
ReadMe.txt
Grunder, Herren, Plüss, 2019
----------------------------------------------------------------------------------------------------

						 ________________________________________________
						/                                                \
					   |    _________________________________________     |
					   |   |                                         |    |
					   |   |  C:\> _                                 |    |
					   |   |                                         |    |
					   |   |                                         |    |
					   |   |                                         |    |
					   |   |              TODO FOR YOU               |    |
					   |   |  A simple Java-based Todo Application   |    |
					   |   |         with REST API interface         |    |
					   |   |         and XML data persistence        |    |
					   |   |                                         |    |
					   |   |                                         |    |
					   |   |                                         |    |
					   |   |                                         |    |
					   |   |_________________________________________|    |
					   |                                                  |
						\_________________________________________________/
							   \___________________________________/
							___________________________________________
						 _-'    .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.  --- `-_
					  _-'.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.--.  .-.-.`-_
				   _-'.-.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-`__`. .-.-.-.`-_
				_-'.-.-.-.-. .-----.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-----. .-.-.-.-.`-_
			 _-'.-.-.-.-.-. .---.-. .-------------------------. .-.---. .---.-.-.-.`-_
			:-------------------------------------------------------------------------:
			`---._.-------------------------------------------------------------._.---'
									 -Ascii Art by Roland Hangg-



____________________________________________________________________________________________________

	
	designed by N. Grunder, G. Herren, F. Plüss at BFH for CAS SD 2018/2019, WepApp course.

____________________________________________________________________________________________________

Content:

1. Requirements

2. How to set up TODO FOR YOU

3. How to use the TODO FOR YOU web interface

4. How to use the TODO FOR YOU REST interface

5. Folder structure and comments

6. Challenges, remarks and ideas

____________________________________________________________________________________________________


1. Requirements

Functional Requirements:

-Each user should be able to maintain a list of todos, i.e. create new todos, update todos, and 
delete todos.
-A todo consists of a title and may have a category and a due date. In addition, a todo 
may be marked as important or completed.
-Todos with a due date should be listed first and sorted by date, and overdue todos should be 
highlighted. In addition, it should be possible to display only the todos of a particular category.


Non-functional Requirements:

-The todos should be protected by a login such that no user can access the todos of another user.
-The todos should be persisted such that the todos are still available after a server restart.
-The graphical user interface should be easy to use and responsive.


Architecture:

The application consists of two parts:
-a classical web application which processes HTTP requests (using Java Servlets) and generates HTML 
responses (using JavaServer Pages)
-a RESTful web service which provides a programming interface that can be used to implement client 
side web application (using JavaScript)

____________________________________________________________________________________________________


2. How to set up TODO FOR YOU

TODO FOR YOU is available as a download on Github. If you have downloaded the full project, it is
best to open it as a INTELLIJ project.

It is based on the Java Servlet technology, with Java Server Pages and JSTL.

The persistence is done with JAXB and XML files for users and todos.

The libraries that have been used are listed under "/Todo_Servlet_Web_App/lib/".

Application Server: Tomcat 9.0.13

HTTP port: 8080

JMX port: 1099

____________________________________________________________________________________________________


3. How to use the TODO FOR YOU web interface

If you open TODO FOR YOU on a web server, you start with the Login page.
You have to have Cookies enabled in your browser to get the right user session.
If you have no account yet, simply put in a user name and password. your account will be created.
You get to the main site. There you can:
-add todos by clicking on the '+' symbol in the upper right corner
-set these todos to "completed" or "not completed" by clicking the rectangle on the left side of
a todo.
-edit or discard the todos by clicking on the symbols on the right of a todo.
-filter todos by category with the drop down menu (click the refresh symbol for the filtering
to take effect)
-search for todos with the search field

The todos are automatically filtered after:
-completed or not completed
-due date

Log out by hovering over your name in the right upper corner and clicking "Log out"


____________________________________________________________________________________________________


4. How to use the TODO FOR YOU REST interface


____________________________________________________________________________________________________


5. Folder structure and comments

(only important folders are listed below)

Servlet_app [todo]/
+-- .idea/
¦   
+-- lib/
¦   
+-- out/										 |-> The code is compiled in this directory.
¦   +-- artifacts/
¦   ¦			+-- todo_war_exploded/
¦   ¦			¦			+-- WEB-INF/
¦   ¦			¦			¦		+-- data/    |-> contains the live XML files with login  
¦   +-- production/								 |	 credentials and a folder for every user 
¦ 												 |   with the todos in a separate XML file.
¦												 -------------------------------------------
+-- src/										 |-> the src file holds all the code files
¦   +-- data									 |   and XML blueprints.
¦   +-- jsonData
¦   +-- org.todo
¦   +-- users
+-- web/										 |-> contains all the .jsp-files and 
¦   											 |   stylesheets
+-- .gitignore/
¦   
+-- ReadMe.txt/
¦   
+-- todo.iml/
¦   
+-- Todo_example.xml/
¦   
+-- Todo_example.xsd/
¦   
+-- External Libraries/
¦   
+-- Scratches and Consoles/



____________________________________________________________________________________________________


6. Challenges, remarks and ideas

General remarks:

We chose a "front controller" paradigm. This means, one Servlet takes the role of managing all the
requests coming from the JSP pages. Additional classes and objects help the program to run.

A hidden HTML tag is given with every "submit" form, to indicate to the Servlet where to go
in the program.

A second servlet takes care of the RESTful web service.

In the src/ folder, there are 4 subdirectories:
-data/ 		-> takes care of the persistence of the todos
-jsonData/ 	-> takes care of the data for the RESTful web API
-org.todo/  -> the main directory
-users/		-> takes care of the persistence of the users

org.todo:

The central classes are "TodoServlet" and "RestServlet". The auxiliary classes can be found under
the "auxiliary" and "auxiliary_REST" directories.

JSP:

With the help of Java Server Pages and JSTL, we can send dynamic information to be displayed on
the web pages. The todos for example are listed in a dynamic table, which adapts its size and
content according to the needs of the information in the XML pages.

Stylesheet:

The stylesheet is taken from https://www.w3schools.com/ and used to make 
the page responsive and dynamic.

Persistence:

Persistence is given by the fact that all information is stored in XML files with JAXB.

JAXB:

Java Architecture for XML Binding (JAXB) is a software framework that allows Java 
developers to map Java classes to XML representations. JAXB provides two main features: 
the ability to marshal Java objects into XML and the inverse, 
i.e. to unmarshal XML back into Java objects. Furthermore, with a batch file, we can
auto-generate Java classes from XML files.

Sorting:

To sort the todo entries after due date, we chose a simple selection sort.
It should be sufficient for the application.

Challenges:

The user persistence in a session, date manipulations and sorting, 
REST services and debugging were steps that took us longer than expected. 

Possible next steps:

The code has several redundancies, i.e. the sorting mechanism is called
twice when initializing a login.

There are some .java files that are not used anymore.

General tidying up of the code, maybe creation of more auxiliary classes.
____________________________________________________________________________________________________