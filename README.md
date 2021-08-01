# Image-Gallery

## What is the app about:
Image-Gallery is a Kotlin based Android Application to search and browse online gallery where user can search images from the application. The application use [Pixabay api](https://pixabay.com/api/docs/) to query the images.

## Architecture Library included
The app is build on a single activity architecture follows the MVVM architecture with the latest android architecture components and jetpack libraries. The app uses Coroutines for concurrency, Retrofit-2 for network calls, Paging-3 for pagination, Hilt for the DI, and for navigation Navigation library from jetpack is used.

## Application overview:
The application uses the [Pixabay api](https://pixabay.com/api/docs/) for querying the images and showing the results in the gridview recyclerview.

**Home Screen :**
The home screen is the place wheare the search view take the query and the web api calls the [Pixabay api](https://pixabay.com/api/docs/) that returns hundreds of images in a single reaponse, to make it simple and load the image with paging feature Paging-3 library is used and the app show the result very efficently without any lags.

Screen:

![home-gallery](https://user-images.githubusercontent.com/12855993/120138279-d0d89280-c1f3-11eb-87b0-ab3c658ca689.png)





**Details Screen :**
On click of the image the app show the details screen with more info of the image like: 
The user who uploaded the image with the user name and user profile image,  
Number of views on the image,   
Number of favourites on the image, 
Number of likes on the image. 
The app also show the web source of the image like the google search shows in the image search result and redirect the user to the source page.

Screen:

![image-details](https://user-images.githubusercontent.com/12855993/120138549-4e040780-c1f4-11eb-8af9-34c38c21e636.png)


**No Internet Screen :**
The application uses a dynamic BroadcastReceiver to listen the network connection changes and redirect the user to the no_internet screen as soon as internet is disconnected, If the connection is retain back the app will automaticaly redirect the user form no_internet screen to the screen where he/she left before the internet connection was broken. 

Screen:

![no_Innternet](https://user-images.githubusercontent.com/12855993/120140812-f3b97580-c1f8-11eb-8d57-651cf2d9445c.png)


