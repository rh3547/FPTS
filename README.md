# RIT SWEN-262 Section 3 - Spring 2016
Team Money Team - Financial Portfolios Tracking System
Class: Engineering of Software Subsystems (SWEN-262)

##Financial Portfolios Tracking System (FPTS)
The Financial Portfolios Tracking System (FPTS) provides the ability to track financial portfolios containing: equities
such as stock, bond and mutual fund holdings; money market accounts; and bank accounts. FPTS can track multiple
accounts each with individual or joint ownership. The system can simulate market conditions to show the effect on
the user's portfolio.


## Introduction
The Financial Portfolios Tracking System (FPTS) provides the ability to track financial portfolios containing: equities such as stock, bond and mutual fund holdings; money market accounts; and bank accounts. FPTS can track multiple accounts each with individual or joint ownership. The system can simulate market conditions to show the effect on the user's portfolio.

## Starting up the System
Navigate to the folder where the jar is located.  There is a file "start.bat" contained in the same folder.  Double click it to startup the program.  An alternative to startup is navigating to the folder containing the .jar file inside of Command Prompt or Terminal and calling the command "java -jar swen262.jar" this will have the same affect. 

## Logging into the system
We've provided a convenient account for you to test with, the credentials are below.
	
	Username: gh1823	Password: password
	
	Username: rhochmuth	Password: password
	
## System Functions
Here are a few small explanations concerning some of the more unclear system interactions:
	Transferring money between cash accounts:
		To transfer money from one cash account to another you need go into the detail view on the source cash account.  Provide an amount in the text field and then select the target or destination account from "transfer to" drop down and then select "Withdraw", the money will be withdrawn from the account you are currently viewing and moved into the targeted account. 
		
	Deleting a User:
		The requirements detailed the need to allow an administrator the functionality to delete a user on the system.  In order to delete a user you start up the program and return to the console, where the program is running. Using the command "-delete user-id-target" where user-id-target is the target user of the delete command.  If the user exists they will be deleted from the system. 
		
	Watch list:
		Navigate to the watch list page.  Select the stock you wish to watch.  Select and upper trigger and a lower trigger.  If the stock price rises above or drop below these bounds you will be alerted with a message at the bottom of the screen.  When you return to the watch list page there will be an “!” next to all of the stocks that have been triggered.
	Market updating:
		If you wish to change the interval in which the market updates from yahoo, go to “File” in the top left and select “Set Market Interval” and type in the desired interval in seconds.

## Functional limitations
The current implementation of the project features a sound design and consistent runtime results.  However there were cases during the testing phase of the project that revealed some edge cases that while they did not affect runtime, would print errors to the console. One such situation was on startup, the project would load the login window, and work as expected, however the console would print out a stack trace identifying a "NullPointerException". 
Another limition of the project is in the simulation portion, this however comes from the way in which Java manages it's float numbers.

## Supporting Files
Source Files:
There is a digital copy of our source provided at the directory: "./src/ftps/"

## Yahoo Integration
First we load the market from the csv so that we know all of the ticker symbols for all of the stocks in the system.  Then we loop and query a yahoo API for the current price of the stocks and update the market.  We chunk multiple stocks into threads and run them to update the market.  Each thread has a chunk of the market so that it is most efficient.  The user is able to select the interval for the market to continue updating. When we update we use visitor and iterator to go through portfolio and update the values of each equity held in the portfolio.  Also watch list checks if any of the triggers have been tripped. 

Library Files:
There are files that support the project, such as fonts, images, and encrypted user information in the folder "src/lib".  These files are required for running the project on the current path, this path is (in relation to the .jar file): "./src/lib/"

Exported Files:
The files that were exported from our system (and can be imported), are located at the root level (with the .jar file) and are named:
	gh1823_export.csv
	rhochmuth_export.csv
