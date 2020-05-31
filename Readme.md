# Peer to peer chat app

This app is used for communication between threads. 
This is peer to peer type of communication where we don't have client and server but equal threads(peers).
For exchanging messages, JSON object is used instead of XML because of its better packaging of data on web.
Project is structured in four classes:

- PeerMain class is main class used for showing GUI and starting communication
- ServerThread class is class for creating socket, creating threads set and starting new thread.
- ServerThreadThread class is used for sending message
- PeerThread class is used for writing message on output stream.

## About project:

First, user needs to login with username and password. There is rule for password: must have more then 4 characters. After that screen, user should enter his username and port. There is check that username from login form matches username from console. 

Next step is to ask for contacts that you want to communicate with.
User should enter contact details in form: <host-name>:<port>
User can enter more then one contact that he wants to communicate.

Next user can choose between next options:
- s: Skip chat
- c: Update contact's list
- e: Exit chat

## Impediments:

- Finding a way to communicate between threads.
- Integrating GUI for showing chat. Coudn't success to implement chat that will show message in right chat box.