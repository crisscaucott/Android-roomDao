## Android-roomDao
## Overview
Show words loaded in database throught a RecyclerView. Items swipe left or right delete the word from the database and click items to update the word.
New words are ingresed by the Floating Button (+). A word item color effect, of the RecyclerView, is triggered when the related word is inserted or updated in the database.
The database is implemented by [Room persistence library](https://developer.android.com/training/data-storage/room/accessing-data) and LiveData, there exists a instrumented test that check the alfabetic order of the words in the database. 


![RoomDao Preview](https://rawgit.com/crisscaucott/Android-roomDao/master/screenshoots/roomWordsApk.gif)
