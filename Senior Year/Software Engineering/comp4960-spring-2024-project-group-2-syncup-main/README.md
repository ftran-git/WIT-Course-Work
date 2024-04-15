# SyncUp

## Introduction
### Summary
- SyncUp is an application built upon [Solid](https://solidproject.org/) specifications where users can upload data about their schedule and availability. Other people in the user's social network can also use our application and provide their data as well which our application will use to help in recommending dates and times to establish event plans. ​
- Sample screenshot from SyncUp: ![image](https://github.com/teachingworkshops/comp4960-spring-2024-project-group-2-syncup/assets/71521018/9261c3b5-563b-48a8-a095-6f9929e525bc)

### Problem w/ Scheduling
- Absence of Centralized Planning: Due to busy and dynamic schedules, users have trouble planning events and meetings with one another and often struggle to accommodate everyone’s availability with a lack of a centralized source of information. 
- Inefficient Communication: Without centralized planning, reaching out to members individually leads to excess time and effort. 
- Overlapping of Events: Struggles in coordination could lead to mistakes and the creation of events that overlap with one another or situations where invitees are unable to attend. 
### Solution & Characteristics 
- The SyncUp application will allow people to be able to join virtual social networks/groups and see when others are available for potential plans​
- The SyncUp application will create and save events into a digital calendar at the request of users
- The SyncUp application will help in recommendations to users with regards to date and time for events​
- The SyncUp application will be built upon Solid specifications which lets individuals and groups store their data securely in decentralized data stores called Pods. Pods are like secure web servers for data. When data is stored in a Pod, its owners control which people and applications can access it [(SolidProject.org)](https://solidproject.org/).

## Pre-Requisites 
### Setting up Solid Pod w/ Inrupt's PodSpaces
- PodSpaces by Inrupt will provide users with a WebID and Solid Pod within Inrupt's Enterprise Solid Server to use with the SyncUp application. Furthermore, Inrupt's Application Registration will allow SyncUp to be authorized and access a user's solid Pod on their behalf.
- Sign up for [PodSpaces](https://start.inrupt.com/profile) to get a valid WebID Solid Pod, then register SyncUp under Application Registration to get a valid Client ID and Client Secret credentials which will be used to log into the application.
- Read more about sign up process directly from [Inrupt](https://docs.inrupt.com/pod-spaces/getting-started/). ![image](https://github.com/teachingworkshops/comp4960-spring-2024-project-group-2-syncup/assets/71521018/fe75087a-9490-4ac6-b928-a0c439c900f1)
- Read more about the application registration process directly from [Inrupt](https://docs.inrupt.com/ess/latest/services/service-application-registration/). ![image](https://github.com/teachingworkshops/comp4960-spring-2024-project-group-2-syncup/assets/71521018/bd9a9e6c-2881-4adf-95d3-f1f0786010a9)
  
### Java Installation
- Make sure Java Development Kit is installed to run the SyncUp .jar file (the latest version is recommended).
- The link to latest JDK from Oracle can be found [here](https://www.oracle.com/java/technologies/downloads/).

## How to Build
### Maven
- This project uses Maven as its management tool so to build the project use the Maven commands "clean" and then "package" to create the latest build in the format of a .jar file. This output will be saved in the "Target" folder of the root directory of the project. To build and start the application directly from a developer's IDE you can use the maven command "springboot:run".

## How to Run
### Window Users
- Head to [Latest Release](https://github.com/teachingworkshops/comp4960-spring-2024-project-group-2-syncup/releases/tag/v1.0) and download the .zip file ending with "Win" for SyncUp.
- Extract and open folder.
- Double-click on the Windows Batch File (.bat extension) which will start the program.
- Alternatively, you can use the command: "java -jar", followed by the name of the .jar file to start the application if you choose to download the .jar file ending with "Win" instead of the .zip file (ensure the .jar file is in the same working directory as the command execution or else provide the path to the file in the command).

### Mac Users 
- Head to [Latest Release](https://github.com/teachingworkshops/comp4960-spring-2024-project-group-2-syncup/releases/tag/v1.0) and download the .jar file ending with "Mac" for SyncUp.
- Open Terminal.
- Use the command: "java -jar", followed by the name of the .jar file to start the application (Ensure the .jar file is in the same working directory as the command execution or else provide the path to the file in the command).

## Credit
### Project Members
- Jillian Desmond, desmondj2@wit.edu
- Thomas Michael, michaelt@wit.edu
- Nilay Patel, pateln19@wit.edu
- Fabio Tran, tranf@wit.edu
### Course & Professor
- Software Engineering at Wentworth Institute of Technology w/ Professor Joshua Gyllinksy, gyllinskyj@wit.edu

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for more details.
