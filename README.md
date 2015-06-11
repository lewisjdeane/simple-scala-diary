# Simple Scala Diary

A simple diary application for your desktop written in scala.
Please note to be able to download and use this you will have to have scala installed and callable from the terminal.

## Download and set up

Simply download the zip folder from https://www.dropbox.com/s/jkx8m4grgbnkf77/Simple%20Scala%20Diary.zip?dl=0
Now unzip the folder, and you will have everything you need.

## Running the application

Open your terminal and navigate to the folder you downloaded with all the files in, so your command prompt should look like .....\Simple Scala Diary>
Finally, run 'scala Diary' and the application will launch. Upon running the first time, you will be asked to set up a password.
Now every time you run the application using the above steps you will be asked to enter this password to enter the diary window interface.

## Using the application

The application is relatively straightforward.
To create a new entry, click new entry, then enter your title in the top field on the right, and add your entry in the space below and click 'Add Entry' to add this entry.
To view an entry, simply click on it in the list on the left and you will be able to read it, edit it or delete.
To edit an entry, click the entry you want and then change it as you want and click 'Edit Entry'.
TO delete an entry, click the entry you want to delete and then click 'Delete Entry'.

## Security

This application provides some default security measures to prevent unwanted eyes viewing your private diary. Password entry means someone can't get in. Your password is saved as a hash in the folder. You can delete the password file which will automatically wipe the diary entries and will require you to set a new password next time you run the application. Also diary entries are saved in a directory within the folder, they are encrypted lightly, and will prevent anyone reading your messages from the plaintext file.
NOTE: I do not take responsibility for the level of security enforced by the encryption, it is a simple XOR encryption and could be broken by someone with enough time and knowledge, the aim of this encryption was to stop people simply reading the plaintext files. I will perhaps one day enforce stricter encryption in this program.
