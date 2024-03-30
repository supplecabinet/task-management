#Task Management App Documentation

[Backend Code Setup]

1} Software Requirements

	Spring Boot 3.2.4 requires Java 17 and is compatible up to and including Java 22. Spring Framework 6.1.5 or above is also required.

	Explicit build support is provided for the following build tool:

	Build Tool 	Version

	Maven       3.6.3 or later
	
	IDE : IntelliJ IDEA (preferred) or Eclipse or whatever you like

2} Installing Spring Boot

	Spring Boot can be used with “classic” Java development tools or installed as a command line tool. 
	Either way, you need Java SDK v17 or higher. Before you begin, you should check your current Java installation by using the following command:

	java -version


3} Maven Installation

	Spring Boot is compatible with Apache Maven 3.6.3 or later. If you do not already have Maven installed, you can follow the instructions at maven.apache.org.

4} Prerequisites

	Before we begin, open a terminal and run the following command to ensure that you have a valid version of Java installed:

	java -version
	
	Sample Output:  java version "17.0.9" 2023-10-17 LTS
					Java(TM) SE Runtime Environment (build 17.0.9+11-LTS-201)
					Java HotSpot(TM) 64-Bit Server VM (build 17.0.9+11-LTS-201, mixed mode, sharing)

	(versions above are just for reference and does not need to match exactly)

5} Setting up task management springboot App

	* Clone the entire application from my public GitHub repository: https://github.com/supplecabinet/task-management
	* Fire up your IDE and import the project from the cloned directory in your local file system.
	* Configure default SDK to the above mentioned Java version.
	* Build the application from maven lifecycle: mvn clean package
	* After successfully built, start the Aplication from the main class located at: com.test.tasks.TaskManagementApplication
	* It ideally takes only a few seconds to start up.

#--------------------------------------This concludes backend code setup.--------------------------------------------

[Frontend Code Setup]


1} Software Requirements

	NodeJS (version 16.20.2)
	npm (version 8.19.4)
	IDE : Visual Studio Code (Latest Version)
	
	NodeJS Official packages for all the major platforms are available at https://nodejs.org/en/download/
	After downloading the mentioned version and installing it, npm will automatically be installed since it is included in the same package.
	
	
2} Setting up frontend Code

	* Start Visual Studio Code (VSCode) and import the "react-frontend" folder present in the cloned directory of your local file system.
	* Open a terminal from VSCode and run the command : npm install [Note: This is a one time step]
	* Upon completion, run the command : npm start 
	* This will start up the frontend code in developer mode and a browser will be opened which should display simple login page.
	* (Make sure springboot backend project is running)
	* Use the following credentials to login to the task management app and explore the features:
	
	User ID          Password
	--------------------------
	tester1		pestotech
	tester2		Mypass90@#2
	
3} Refer to README file under react-frontend directory for more information.

#--------------------------------------This concludes frontend code setup-------------------------------------------------

#--------------------------------------Planned future updates-------------------------------------------------------------

1} User Registration - (Email will be Sent for Confimation)
2} Task End Date Reminder
3} Enhanced Login UI
4} History of every task updation