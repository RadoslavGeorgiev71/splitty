# OOPP Template Project

## Using the tag menu:
> The tag menu in its simplicity may have some ambiguity. Here are some useful instructions
> * To open the tag menu press the Tags button in the add expense or edit expense window
> * To select a tag for an expense just choose one from the menu and then press select
> * To add a new tag edit the tag you are currently on and press add - an entirely new tag will be created
> * To edit a tag make changes to the current tag and press edit
> * To delete a tag select the desired tag and press delete

## Debts design choices:
> Here are some design decisions that are implemented for open debts.
> * When you mark a debt as received it is not present in the future(To add it anew you can create an expense between the same people)
> * When you delete a participant all of their related debts are removed
> * If you mark a debt as received but then delete the expenses it originates from - a new debt will be displayed, one that reverses the change and makes sure the net amount of the participants is fair

## Keyboard Shortcuts:
### Client:
> Start screen 
> * Ctrl + W: close window
> * Enter: depending on where the focus is on create an event or join an event

> Invitation
> * Ctrl + W: close window
> * Enter: send invites
> * Esc: go back to start screen

> User Settings
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to start screen (changes are not saved)

> Event Overview 
> * Ctrl + W: close window
> * Esc: go back to start screen

> Add participant
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Edit participant
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Add Expense
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Edit Expense
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Open Debts
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

> Statistics
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

> Tags
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

### Admin:

> Login
> * Ctrl + W: close window
> * Enter: login  

> Overview
> * Ctrl + W: close window
> * Ctrl + R: refresh events' table
> * Ctrl + UP: select previous event
> * Ctrl + DOWN: select next event