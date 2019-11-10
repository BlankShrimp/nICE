# App Lifecycle
The app will be divided in three activities.

## EntryPoint Activty
This will check if the user has authenticated or not. If it has, it will launch the main activity, otherwise it will launch the login activity. This is the fastest approach to check for login, without even loading a layout file.

## Login Activity
This will have very simple login functions. Methods to communicate with the server must be implemented outside of this, in another package.

## Main Activity
This will provide all the functionalities of the app. It will be divided in three main parts: module-level settings, activity log, global settings. Helper methods (writing to file, communicating with the server, services...) must be put elsewhere



# User Interface

## Login Activity
Simple "username" "password" fields and a login button

## Main Activity
The layout will have a bottom navigation bar with three options as specified above.

### Module Settings
There will be a scrollable view of tiles. Every module will have a tile of its own, covering the width of the screen. In each tile there will be settings to:
- download materials
- get notifications of new files
- add deadlines to calendar
- *Optional* upload attendance code

### Activity Log
This will display a list of what the app has done. Each item in the list will be made of the timestamp and the action taken. For example:
- '11.11 00:10' -  'Ate a raw potato because I have no money for chips'
- '11.11 00:05' -  'Bought everything in my Taobao cart'

### Global Settings
This will allow one to pick which modules to consider. So all the continuous support / unknown departments bullshit will be filtered out.
It will also show the user's image, name and student ID on top

# Data Models
The user's authentication credentials / tokens will be stored as SharedPreferences.

Settings can be saved in either a text file or in a Database (preferred choice). Same applies to logs and module-related data

### Module
Represents a module with all its settings and content.
- title
- enable auto download (Y/N)
- enable notification (Y/N)
- more settings...
- files list
- events list

### File
Represents a single file. It needs some infos to manage the downloads well
- filename
- parent path
- download date

### Event
Represents a deadline (is there anything else it can represent?)
- date / date+time
- added to calendar (Y/N)
- needs notification (how to implement this is a future decision)



# Server Communication
I love spaghetti


# Integration
Connect it all together with a spaghetti