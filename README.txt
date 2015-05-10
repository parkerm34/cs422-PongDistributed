Client Side Inputs
===================
Pong Client
-----------
Needs string that is the hosts IP on startup, gotten through command line.

Needs input to declare the ball array. I think we should do this on the server
	and implement it by having a function on the server that redefines the ball array
	given an input and a player can do it at any time by clicking a button the resets the
	game with the number of balls specified.... if you want to do it through a command
	line argument to make it easier then by all means. Command line argument should probably
	be the first idea to do, and figure out the former later.
	
PongGUI
-------
For the inputs of PongGUI, every time we want to redraw,
	we are going to need the positions of the balls currently as well as
	the position of the paddle. Currently this is done through getting the
	balls from the bodies array in the client. The paddle is stored in the gui
	and will need its location sent through the socket(output).
	
Client Side Outputs
====================
Pong Client
-----------
Needs to return the position of its paddle to the server to the server can update
	the clients of the position
Needs to send the SIZE of the window at least once...
	
OptionGUI
---------
Returns which side the client has chosen if we decide to go this route

Might also return a specified message to re-initialize the game with number of balls specified
	(This option will change up how the optionGUI is handled, or I might use a different GUI,
	let me know when you are done implementing most of the client/server messages and I will
	figure this out once you have most of it figured out.)
	
Server Side Inputs
===================
Pong(Sub?)Server
----------
Going to be receiving inputs about position of paddles, number of balls, number of processes,
	also needs to ensure the client windows spawned are the same size (trivial but need
	to ensure the calculations are going to work. I will make the windows not able to be resized.)

Server Side Outputs
====================
Pong(Sub?)Server
----------
Will need to return to the clients:
	- paddle position for each client
	- ball position for each client/array(object) of balls

	These will be needed every time the update is re-rendered, which is every time a ball moves. 
	
	- let clients know who won and lost allowing a new popup for a message for each
	- let clients know when a round is over
	- let clients know the score

	These can be special messages appended to the end so the windows know when to render these
		and possibly pause animation.

OTHER INPUTS AND OUTPUTS
=========================
See PongController for other information on these. These are more command line arguments rather than
	stuff going through the sockets, so I felt like they didnt need to go here. But they are involved
	with all of this.



IMPORTANT UPDATE ON HOW TO RUN JAVA IMPLEMENTATION
==================================================
make PongController
java -cp bin controller.PongController


Then the usage will let you know how to use command line arguments

This is because java has weird command line dependencies when using packages
just copy and past this line and it will work.


