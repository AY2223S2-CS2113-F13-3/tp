# User Guide

## Table of Contents
- [Introduction](#7-introduction)
- [Quick Start](#14-quick-start)
- [Features](#25-features)
- [FAQ](#100-faq)
- [Command Summary](#113-command-summary)

## 1.0 Introduction
🗓️ NUSPlanner is a desktop app that allows for an *easy* and *straightforward* way for NUS students to manage their schedule ranging from person, school or external related activities. This application makes use of a desktop Command Line Interface (CLI), enabling a quick and sleek method of getting your schedule in check.

⌨️ If you can type fast, NUSPlanner can get your contact management tasks done faster and more efficiently than traditional GUI apps.

✅ NUSPlanner is available for download for operating systems such as Windows, Linux and OS-X.

## 2.0 Quick Start
Before we get started, here's what you need to do:

1. Ensure that you have Java 11 or above installed.
2. Down the latest version of `NUSPlanner.jar` from [here](http://link.to/NUSPlanner.jar).
3. Open your terminal or command prompt
4. Navigate to the file directory of where the jar file is saved
5. Execute `NUSPlanner.jar` using the following command: `java -jar NUSPlanner.jar` 
6. Refer to section [3.0 Features] for more details of commands

## 3.0 Features 

Read this section to find out how NUSPlanner can make your life easier!

### Adding an event: `add`
Adds an event to the schedule. Assuming x is an switch. Use –x to specify the attributes to edit. 

Format: `add –e EVENTNAME –st STARTTIME –sd STARTDATE –et ENDTIME –ed ENDDATE`
* sd and ed must be of the format YYYY/MM/DD
* ed is an optional field, where the default value is sd when not specified

#### Examples of usage
* Add event that starts and ends on different days: `add –e Career Fair –st 14:00 –sd 2023/02/10 –et 16:00 –ed 2023-02-11`
* Add event that starts and ends on same day: `add –e Career Fair –st 14:00 –sd 2023/02/10 –et 16:00`

Expected outcome:
~~~
____________________________________________________________
Event successfully added!
____________________________________________________________
~~~

### Deleting an event: `delete`
Delete a single event or all events from the schedule. Use –s to specify the events to delete, or –all to delete all events.

Format:
* `delete –s INDEX_OF_EVENT `
* `delete -all`

#### Examples of usage
* Delete a single event: `delete –s 1`
* Delete all events: `delete -all`

Expected outcome:
~~~
____________________________________________________________
Event(s) successfully deleted!
____________________________________________________________
~~~

### List an event: `list`
Displays a list of all events that have been added to the schedule.

Format: `list`

#### Examples of usage
* List all events: `list`

Expected outcome:
~~~
____________________________________________________________
Here are your event(s):
1. CS2100 Tutorial on 18/03/2023 from 3pm to 5pm
2. Penetration Testing Bootcamp on 19/03/2023 to 23/03/2023 from 12pm to 6pm
____________________________________________________________
~~~

### Editing an event: `edit`
Edit an event that has been added to the schedule. Use –x to specify the attributes to edit.

Format:
* `edit -i INDEX_OF_EVENT –st STARTTIME –sd STARTDATE –et ENDTIME –ed ENDDATE`

#### Examples of usage
* Edit the start time of an event: `edit –i 2 –st 16:00`
* Edit the start date, start time and end time of an event: `edit -i 3 –sd 2023/02/11 –st 8:00 –et 10:00 `

Expected outcome:
~~~
____________________________________________________________
Event edited successfully!
____________________________________________________________
~~~

## FAQ

**Q**: Why is a JAR file required?

**A**: Java ARchive, also known as JAR, is a container that groups multiple small files to enable efficient execution, which is similar to a ZIP file. 


**Q**: Where can I report bugs I found while using NUSPlanner?

**A**: Our team of developers are always on the look-out to fix such bugs.
Please add a description of the bug found under our [Issue Tracker]([here](https://github.com/AY2223S2-CS2113-F13-3/tp/issues)).

## Command Summary
👉 Words in **CAPITAL LETTERS** are user input

👉 Words in [] are OPTIONAL

👉 Each word is separated by only a **single whitespace**


**Command | Description**
* `add –e EVENTNAME –st [STARTTIME] –sd STARTDATE –et [ENDTIME] –ed [ENDDATE]` | Add event
* `delete –s 1` | Delete a single event
* `delete -all` | Delete all events
* `list` | List all events
* `edit -i INDEX_OF_EVENT –st [STARTTIME] –sd STARTDATE –et [ENDTIME] –ed [ENDDATE]` | Edit event



